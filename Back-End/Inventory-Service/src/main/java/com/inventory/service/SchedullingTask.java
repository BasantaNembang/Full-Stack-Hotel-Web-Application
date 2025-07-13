package com.inventory.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class SchedullingTask {

    @Autowired
    ThreadPoolTaskScheduler scheduler;


    public void SchedulerTask(Runnable runnable, LocalDate checkOutDate){
        Date timeToDelete = Date.from(checkOutDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        //now delete the room
        scheduler.schedule(runnable, timeToDelete);
    }


}
