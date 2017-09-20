package com.dongnao.jack.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dongnao.jack.service.OrderFactory;

@Service("UUIDOrderFactory")
public class UUIDOrderFactory implements OrderFactory {
    
    public String createOrderId() {
        return UUID.randomUUID().toString();
    }
    
}
