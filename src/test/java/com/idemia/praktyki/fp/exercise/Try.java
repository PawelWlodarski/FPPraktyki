package com.idemia.praktyki.fp.exercise;

import java.util.function.Function;
import java.util.function.Supplier;

abstract class Try<A> {

    static <A> Try<A> of(Supplier<A> lazyValue) {
        try {
            return new Success(lazyValue.get());
        } catch (Exception e) {
            return new Failure(e);
        }
    }


    abstract <B> Try<B> map(Function<A, B> f);

    abstract <B> Try<B> flatMap(Function<A, Try<B>> f);

    abstract Try<A> recover(Function<Exception, Try<A>> f);

    abstract <B> B fold(Function<A, B> forSuccess, Function<Exception, B> forError);


    static private class Success<A> extends Try<A> {
        private A value;

        private Success(A value) {
            this.value = value;
        }

        @Override
        <B> Try<B> map(Function<A, B> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        <B> Try<B> flatMap(Function<A, Try<B>> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        Try<A> recover(Function<Exception, Try<A>> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        <B> B fold(Function<A, B> forSuccess, Function<Exception, B> forError) {
            throw new RuntimeException("EXERCISE");
        }

    }

    static private class Failure<A> extends Try<A> {

        private final Exception error;

        private Failure(Exception error) {
            this.error = error;
        }

        @SuppressWarnings("unchecked")
        private <B> Try<B> recast() {
            return (Try<B>) this;
        }


        @Override
        <B> Try<B> map(Function<A, B> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        <B> Try<B> flatMap(Function<A, Try<B>> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        Try<A> recover(Function<Exception, Try<A>> f) {
            throw new RuntimeException("EXERCISE");
        }

        @Override
        <B> B fold(Function<A, B> forSuccess, Function<Exception, B> forError) {
            throw new RuntimeException("EXERCISE");
        }
    }
}
