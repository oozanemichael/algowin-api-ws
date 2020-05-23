package org.mh.service.netty.test;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

public class RxJavaCompletable {

    public static void main(String[] args) {
        Completable.create(new CompletableOnSubscribe() {
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                try {
                    System.out.println("sss");
                    TimeUnit.SECONDS.sleep(1);
                    completableEmitter.onComplete();
                } catch (InterruptedException e) {
                    completableEmitter.onError(e);
                }

            }
        }).blockingAwait();   //blockingAwait 阻塞等待

        Completable.fromAction(new Action() {

            public void run() throws Exception {

                System.out.println("Hello World");
            }
        }).subscribe();


    }

}
