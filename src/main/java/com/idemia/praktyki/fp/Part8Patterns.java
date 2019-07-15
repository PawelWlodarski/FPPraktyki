package com.idemia.praktyki.fp;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Part8Patterns {

    public static void main(String[] args) {

    }

    static Function<Integer,Integer> factory(int base){
        return i -> i+base;
    }

    //In a moment this one will be important
    static <A,B> Function<A,B> decorator(Function<A,B> f){
        return i -> {
            System.out.println("decorated");
            return f.apply(i);
        };
    }

    static <A> void strategy(Function<String,A> strategy){
        Stream<A> result = List.of("aaa","bbbbb","dddd").stream().map(strategy);
        result.forEach(System.out::println);
    }


}
