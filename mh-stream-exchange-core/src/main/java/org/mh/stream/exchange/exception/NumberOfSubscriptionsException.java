package org.mh.stream.exchange.exception;

/**
 * 超过最大订阅数量异常
 * */
public class NumberOfSubscriptionsException extends Exception{

    Integer maximumNumber;

    private static final long serialVersionUID = -8778511865943973516L;

    public NumberOfSubscriptionsException(){
        super();
    }

    public NumberOfSubscriptionsException(String message) {
        super(message);
    }

    public NumberOfSubscriptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NumberOfSubscriptionsException(Throwable cause) {
        super(cause);
    }

    public NumberOfSubscriptionsException(Integer maximumNumber, String message){
        super("The maximum number of subscriptions is "+maximumNumber+" ,"+message);
    }

    public NumberOfSubscriptionsException(Integer maximumNumber){
        super("The maximum number of subscriptions is "+maximumNumber);
    }
}
