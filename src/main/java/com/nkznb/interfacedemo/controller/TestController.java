package com.nkznb.interfacedemo.controller;

import com.nkznb.interfacedemo.annotation.DayDayUp;
import com.nkznb.interfacedemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nkznb.interfacedemo.utils.CommonResponse;

/**
 * @author Can.Ru
 */
@RestController
@RequestMapping(path = "upload")
public class TestController {

    @Autowired
    private TestService tService;

    @DayDayUp
    @PostMapping("/login")
    public CommonResponse<String> login(){
        return CommonResponse.success(tService.upload());
    }
}
