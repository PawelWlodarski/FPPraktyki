package com.idemia.praktyki.fp.answer;

import java.util.function.Function;
import java.util.function.Supplier;

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
