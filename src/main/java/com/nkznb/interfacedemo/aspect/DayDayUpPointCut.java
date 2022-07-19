package com.nkznb.interfacedemo.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Can.Ru
 */
public class DayDayUpPointCut {


    @Pointcut("@annotation(com.nkznb.interfacedemo.annotation.DayDayUp)")
    public void dayDayUp() {

    }
}
