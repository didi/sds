package com.didiglobal.sds.example.api;

import java.util.concurrent.CompletableFuture;

/**
 * Created by sea on 2020-03-28.
 */
public interface HelloService {

    String hello(String content);

    CompletableFuture<String> asyncHello(String content);

}
