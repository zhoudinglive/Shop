package com.shop.Assist;

import com.shop.Service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Carpenter on 2017/3/19.
 */
@Component
public class Scheduler {
    @Autowired
    private RecommendService recommendService;

    @Scheduled(fixedRate = 3600000)
    public void timer(){
        recommendService.userCF();
    }
}
