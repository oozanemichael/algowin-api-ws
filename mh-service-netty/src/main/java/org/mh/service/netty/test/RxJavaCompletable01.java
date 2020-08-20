package org.mh.service.netty.test;

import io.reactivex.Completable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RxJavaCompletable01 {

    private static final Logger log = LoggerFactory.getLogger(RxJavaCompletable01.class);

    public static void main(String[] args) {
        Completable.create(completable -> log.info("ss")).subscribe();
    }

}
