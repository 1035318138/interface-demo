package com.nkznb.interfacedemo.service;

import com.nkznb.interfacedemo.annotation.Upload;
import com.nkznb.interfacedemo.enums.UploadType;
import org.springframework.stereotype.Service;

/**
 * @author Can.Ru
 */
@Service
public class TestService {

    @Upload(type = UploadType.类型1)
    public String upload() {

        return "null";
    }
}
