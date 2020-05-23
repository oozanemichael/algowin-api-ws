package org.mh.service.netty.test;

import io.reactivex.Completable;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RxJavaCompletable01 {

    public static void main(String[] args) {
        Completable.create(completable -> log.info("ss")).subscribe();
    }

}
