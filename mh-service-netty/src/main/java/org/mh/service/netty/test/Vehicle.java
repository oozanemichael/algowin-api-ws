package org.mh.service.netty.test;

public interface Vehicle {
   default void print(){
      System.out.println("我是一辆车!");
   }
}