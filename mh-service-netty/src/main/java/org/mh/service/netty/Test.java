package org.mh.service.netty;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Test {

    public static void main(String[] args) {
        Runnable beforeConnectionHandler= new Runnable() {
            public void run() {
            }
        };
        beforeConnectionHandler.run();
        log.info("sss");

    }

}
