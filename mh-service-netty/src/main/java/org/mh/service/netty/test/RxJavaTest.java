package org.mh.service.netty.test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RxJavaTest {

    private static final Logger log = LoggerFactory.getLogger(RxJavaTest.class);

    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 1. 创建被观察者 & 生产事件
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
                observableEmitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            public void onSubscribe(Disposable disposable) {
                log.info( "开始采用subscribe连接");
            }

            public void onNext(Integer integer) {
                log.info( "对Next事件"+ integer +"作出响应"  );
            }

            public void onError(Throwable throwable) {
                log.info( "对Error事件作出响应");
            }

            public void onComplete() {
                log.info( "对Complete事件作出响应");
            }
        });

    }

}
