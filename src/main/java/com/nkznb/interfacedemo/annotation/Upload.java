package com.nkznb.interfacedemo.annotation;

import com.nkznb.interfacedemo.enums.UploadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Can.Ru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Upload {

    // 记录上传类型
    UploadType type() default UploadType.未知;
}
