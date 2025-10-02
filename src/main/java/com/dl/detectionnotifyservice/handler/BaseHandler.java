package com.dl.detectionnotifyservice.handler;

public interface BaseHandler<T> {
    void consume(T payload);
}
