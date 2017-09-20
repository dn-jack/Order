package com.dongnao.jack.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.dongnao.jack.redis.RedisApi;
import com.dongnao.jack.service.OrderFactory;

@Service("RedisOrderFactory")
public class RedisOrderFactory implements OrderFactory {
    
    public String createOrderId() {
        String prefix = fixOrderIdPrefix(new Date());
        String key = "dongnao_java_jack" + prefix;
        Long orderId = RedisApi.incr(key);
        return orderId != null ? prefix + orderId.toString() : "3W";
    }
    
    private String fixOrderIdPrefix(Date date) {
        Calendar c = Calendar.getInstance();
        
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String dayFmt = String.format("%1$03d", day);
        String hourFmt = String.format("%1$02d", hour);
        return (year - 2000) + dayFmt + hourFmt;
    }
    
}
