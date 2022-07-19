package com.nkznb.interfacedemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

/**
 * @author Can.Ru
 */
@Aspect
@Slf4j
@Service
public class DayDayUpAspect {

    @Around("com.nkznb.interfacedemo.aspect.DayDayUpPointCut.dayDayUp()")
    public Object enhanceDayDayUp(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("正在经过切片增强。enhanceDayDayUp");
        // [1] 查看方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("方法 ={}", methodSignature.getMethod().getName());
        // [2] 查看参数名,参数值
        String[] argNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < argNames.length; i++) {
            log.info("参数 {} = {}", argNames[i], args[i]);
        }
        // [3] 选择操作或者不操作，然后进行返回
        return joinPoint.proceed();
    }
}
