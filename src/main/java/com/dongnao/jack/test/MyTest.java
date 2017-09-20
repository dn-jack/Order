package com.dongnao.jack.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnao.jack.service.impl.RedisOrderFactory;
import com.dongnao.jack.service.impl.Snowflake;
import com.dongnao.jack.service.impl.UUIDOrderFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:config/spring/spring-dispatcher.xml")
public class MyTest {
    
    @Autowired
    @Qualifier("UUIDOrderFactory")
    UUIDOrderFactory UUIDOrderFactory;
    
    @Autowired
    @Qualifier("Snowflake")
    Snowflake orderFactory;
    
    @Autowired
    @Qualifier("RedisOrderFactory")
    RedisOrderFactory redisorder;
    
    // 并发的用户数（同时并发的线程数）
    private static final int threadNum = 100;
    
    // 倒计数器（发令枪），用于制造线程的并发执行
    private static CountDownLatch cdl = new CountDownLatch(threadNum);
    
    private AtomicLong sum = new AtomicLong();
    
    @Test
    public void test2() {
        for (int i = 0; i < threadNum; i++) {
            new Thread(new orderThread()).start();
            cdl.countDown();
        }
        try {
            Thread.currentThread().sleep(2000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("总共线程执行时间：" + sum.get());
    }
    
    @Test
    public void test3() {
        redisorder.createOrderId();
        
    }
    
    class orderThread implements Runnable {
        
        public void run() {
            Long t1 = System.currentTimeMillis();
            try {
                cdl.await();
                for (int i = 0; i < 5; i++) {
                    System.out.println("insert into jack_id values ('"
                            + redisorder.createOrderId() + "');");
                }
                Long t2 = System.currentTimeMillis();
                sum.addAndGet(t2 - t1);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
