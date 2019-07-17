package com.idemia.praktyki.fp.exercise;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

final class EffectsLibrary {

    private EffectsLibrary() {
    }

    static <A, B, C> Optional<C> map2(Optional<A> a, Optional<B> b, BiFunction<A, B, C> f) {
        throw new RuntimeException("EXERCISE");
    }

    static <A, B, C> CompletableFuture<C> map2(CompletableFuture<A> a, CompletableFuture<B> b,
                                               BiFunction<A, B, C> f) {
        throw new RuntimeException("EXERCISE");
    }

    

    static <A> Optional<List<A>> sequence(List<Optional<A>> input) {
        throw new RuntimeException("EXERCISE");
    }


    static <A,B> CompletableFuture<List<B>> traverse(List<CompletableFuture<A>> input, Function<A,B> f) {
        throw new RuntimeException("EXERCISE");
    }

    static <A> CompletableFuture<List<A>> sequenceByTraverse(List<CompletableFuture<A>> input){
        return traverse(input,Function.identity());
    }


}
