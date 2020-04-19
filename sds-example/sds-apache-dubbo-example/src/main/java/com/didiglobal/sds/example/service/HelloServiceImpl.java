package com.didiglobal.sds.example.service;

import com.didiglobal.sds.example.api.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by sea on 2020-03-28.
 */
@Service(interfaceClass = HelloService.class)
@Profile("provider")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String content) {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1,10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "world";
    }

    @Override
    public CompletableFuture<String> asyncHello(String content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1,10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        });
    }
}
