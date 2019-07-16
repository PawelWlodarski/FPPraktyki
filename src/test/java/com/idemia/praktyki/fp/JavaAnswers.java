package com.idemia.praktyki.fp;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public class JavaAnswers {

    @Test
    public void ex1functionalRobot(){
        //given
        Robot robot = Robot.buildRobot(100);
        IntFunction<IntUnaryOperator> takeAHit = i -> energy -> energy - i;
        IntFunction<String> toString = i -> "robot energy is : " +i;

        //when
        var robotState2=robot.modify(takeAHit.apply(32));

        //then
        var originalStateInfo = robot.get(toString);
        var afterHitStateInfo = robotState2.get(toString);
        Assertions.assertThat(afterHitStateInfo).isEqualTo("robot energy is : 68");
        Assertions.assertThat(originalStateInfo).isEqualTo("robot energy is : 100");
    }

    @Test
    public void ex2HighOrderFunctionsPartialApplication(){
        BiFunction<Integer,Integer,Integer> add = (i,j) -> i+j;
        BiFunction<Integer,String,String> repeat = (howMany,content)
                -> Stream.generate(() -> content)
                .limit(howMany)
                .reduce("",String::concat);

        Function<Integer, Integer> addToFour = HighOrderFunctions.partialApplication(add, 4);
        Function<String,String> repeatFourTimes = HighOrderFunctions.partialApplication(repeat,4);

        Assertions.assertThat(addToFour.apply(3)).isEqualTo(7) ;
        Assertions.assertThat(repeatFourTimes.apply("hello")).isEqualTo("hellohellohellohello") ;

    }

    @Test
    public void ex2HighOrderFunctionsCurry(){
        BiFunction<Integer,Integer,Integer> add = (i,j) -> i+j;
        BiFunction<Integer,String,String> repeat = (howMany,content)
                -> Stream.generate(() -> content)
                .limit(howMany)
                .reduce("",String::concat);

        Function<Integer, Function<Integer, Integer>> curriedAdd = HighOrderFunctions.curry(add);
        BiFunction<Integer, Integer, Integer> uncurriedAdd = HighOrderFunctions.uncurry(curriedAdd);

        Function<Integer, Function<String, String>> curriedRepeat = HighOrderFunctions.curry(repeat);
        BiFunction<Integer, String, String> uncurriedRepeat = HighOrderFunctions.uncurry(curriedRepeat);

        Assertions
                .assertThat(curriedAdd.apply(4).apply(5))
                .isEqualTo(uncurriedAdd.apply(4,5));


        Assertions
                .assertThat(curriedRepeat.apply(4).apply("text"))
                .isEqualTo(uncurriedRepeat.apply(4,"text"));

    }

    @Test
    public void ex3CustomOptional(){
        MyOptional<String> longContent = MyOptional.of("content");
        MyOptional<String> shortContent = MyOptional.of("c");
        MyOptional<String> emptyContent = MyOptional.of(null);

        var result1=processOptionalData(longContent);
        var result2=processOptionalData(shortContent);
        var result3=processOptionalData(emptyContent);


        Assertions
                .assertThat(List.of(result1,result2,result3))
                .isEqualTo(List.of("result : 7","EMPTY","EMPTY"));

    }

    private String processOptionalData(MyOptional<String> content) {
        return content
                .map(String::length)
                .flatMap(i -> i>3? MyOptional.of(i):MyOptional.empty())
                .fold(i -> "result : "+i, () -> "EMPTY");
    }

}


class Robot{

    final private int energy;

    private Robot(int energy){
        this.energy = energy;
    }

    static Robot buildRobot(int energy){
        return new Robot(energy);
    }

    Robot modify(IntUnaryOperator f){
        return  buildRobot(f.applyAsInt(energy));
    }

    <A> A get(IntFunction<A> c){
        return c.apply(energy);
    }
}

class HighOrderFunctions{
    static <A,B,C> Function<B,C> partialApplication(BiFunction<A,B,C> f, A parameter){
        return b -> f.apply(parameter,b);
    }


    static <A,B,C> Function<A,Function<B,C>> curry(BiFunction<A,B,C> f){
        return a->b->f.apply(a,b);
    }

    static <A,B,C> BiFunction<A,B,C> uncurry(Function<A,Function<B,C>> f){
        return (a,b) -> f.apply(a).apply(b);
    }

}

abstract class MyOptional<A>{

    static <A> MyOptional<A> of(A value){
        if(value!=null) return new Some(value);
        else return None.INSTANCE;
    }

    static <A> MyOptional<A> empty(){
        return  None.INSTANCE;
    }

    abstract <B> MyOptional<B> map(Function<A,B> f);
    abstract <B> MyOptional<B> flatMap(Function<A,MyOptional<B>> f);
    abstract <B> B fold(Function<A,B> forSome, Supplier<B> forNone);


    static private class Some<A> extends MyOptional<A>{
        private A value;

        private Some(A value) {
            this.value = value;
        }

        @Override
        <B> MyOptional<B> map(Function<A, B> f) {
            return new Some(f.apply(value));
        }

        @Override
        <B> MyOptional<B> flatMap(Function<A, MyOptional<B>> f) {
            return f.apply(value);
        }

        @Override
        <B> B fold(Function<A, B> forSome, Supplier<B> forNone) {
            return forSome.apply(value);
        }
    }

    static private class None<A> extends MyOptional<A> {

        private static None INSTANCE= new None();

        private <B> None<B> none() {
            return INSTANCE;
        }

        @Override
        <B> MyOptional<B> map(Function<A, B> f) {
            return none();
        }

        @Override
        <B> MyOptional<B> flatMap(Function<A, MyOptional<B>> f) {
            return none();
        }

        @Override
        <B> B fold(Function<A, B> forSome, Supplier<B> forNone) {
            return forNone.get();
        }
    }
}