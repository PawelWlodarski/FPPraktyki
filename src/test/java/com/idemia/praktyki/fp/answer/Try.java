package com.idemia.praktyki.fp.answer;

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
            return new Try.Success<>(f.apply(value));
        }

        @Override
        <B> Try<B> flatMap(Function<A, Try<B>> f) {
            return f.apply(value);
        }

        @Override
        Try<A> recover(Function<Exception, Try<A>> f) {
            return this;
        }

        @Override
        <B> B fold(Function<A, B> forSuccess, Function<Exception, B> forError) {
            return forSuccess.apply(value);
        }

    }

    static private class Failure<A> extends Try<A> {

        private final Exception error;

        private Failure(Exception error) {
            this.error = error;
        }

        private <B> Try<B> recast() {
            return (Try<B>) this;
        }


        @Override
        <B> Try<B> map(Function<A, B> f) {
            return recast();
        }

        @Override
        <B> Try<B> flatMap(Function<A, Try<B>> f) {
            return recast();
        }

        @Override
        Try<A> recover(Function<Exception, Try<A>> f) {
            return f.apply(error);
        }

        @Override
        <B> B fold(Function<A, B> forSuccess, Function<Exception, B> forError) {
            return forError.apply(error);
        }
    }
}
