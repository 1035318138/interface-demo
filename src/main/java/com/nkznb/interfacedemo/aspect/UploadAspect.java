package com.nkznb.interfacedemo.aspect;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nkznb.interfacedemo.annotation.Upload;
import com.nkznb.interfacedemo.enums.UploadType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Can.Ru
 */
@Component
@Aspect
@Slf4j
public class UploadAspect {

    public static ThreadFactory commonThreadFactory = new ThreadFactoryBuilder().setNameFormat("upload-pool-%d")
            .setPriority(Thread.NORM_PRIORITY).build();
    public static ExecutorService uploadExecuteService = new ThreadPoolExecutor(10, 20, 300L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), commonThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    @Pointcut("@annotation(com.nkznb.interfacedemo.annotation.Upload)")
    public void uploadPoint() {}

    @Around(value = "uploadPoint()")
    public void uploadControl(ProceedingJoinPoint pjp) {
        log.info("sss");
        // 获取方法上的注解，进而获取uploadType
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        Upload annotation = signature.getMethod().getAnnotation(Upload.class);
        UploadType type = annotation == null ? UploadType.未知 : annotation.type();
        // 获取batchNo
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        // 初始化一条上传的日志，记录开始时间
        //writeLogToDB(batchNo, type, new Date)
        // 线程池启动异步线程，开始执行上传的逻辑，pjp.proceed()就是你实现的上传功能
        uploadExecuteService.submit(() -> {
            try {
                String errorMessage = pjp.proceed().toString();
                // 没有异常直接成功
                if (StringUtils.isEmpty(errorMessage)) {
                    // 成功，写入数据库，具体不展开了
                    //writeSuccessToDB(batchNo);
                } else {
                    // 失败，因为返回了校验信息
                    fail(errorMessage, batchNo);
                }
            } catch (Throwable e) {
                log.error("导入失败：", e);
                // 失败，抛了异常，需要记录
                fail(e.toString(), batchNo);
            }
        });
    }

    private void fail(String message, String batchNo) {
        // 生成上传错误日志文件的文件key
        String s3Key = UUID.randomUUID().toString().replace("-", "");
        // 生成文件名称
        String fileName = "错误日志_" +
                UUID.randomUUID()+".xlsx";
        String filePath = "/home/xxx/xxx/" + fileName;
        // 生成一个文件，写入错误数据
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(message.getBytes());

        } catch (Exception e) {
            log.error("写入文件错误", e);
        } finally {
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (Exception e) {
                log.error("关闭错误", e);
            }
        }
        // 上传错误日志文件到文件服务器，我们用的是s3
        //upFileToS3(file, s3Key);
        // 记录上传失败，同时记录错误日志文件地址到数据库，方便用户查看错误信息
        //writeFailToDB(batchNo, s3Key, fileName);
        // 删除文件，防止硬盘爆炸
        //deleteFile(file)
    }

}
