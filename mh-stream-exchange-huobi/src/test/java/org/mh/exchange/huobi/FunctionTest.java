package org.mh.exchange.huobi;

import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;

public class FunctionTest {

    public static void main(String[] args) {

        Function<String, String> function = a -> a + " Jack!";
        try {
            ObjectHelper.requireNonNull(function.apply("Hello"), "The mapper function returned a null value.");
            System.out.println(function.apply("Hello"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
