package com.idemia.praktyki.fp.answer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class EffectsLibrary {

    private EffectsLibrary() {
    }

    static <A, B, C> Optional<C> map2(Optional<A> a, Optional<B> b, BiFunction<A, B, C> f) {
        Optional<C> c = a.flatMap(av ->
                b.map(bv -> f.apply(av, bv))
        );

        return c;
    }

    static <A, B, C> CompletableFuture<C> map2(CompletableFuture<A> a, CompletableFuture<B> b,
                                               BiFunction<A, B, C> f) {
        CompletableFuture<C> c = a.thenCompose(av ->
                b.thenApply(bv -> f.apply(av, bv))
        );

        return c;
    }

    private static <A> BiFunction<List<A>,A, List<A>>  appendFunctionFactory(){
        return  (l,e) -> {
            l.add(e);
            return l;
        } ;
    }

    static <A> Optional<List<A>> iterate(List<Optional<A>> input) {
            Optional<List<A>> result=Optional.of(new LinkedList<>());

            BiFunction<List<A>,A, List<A>> append = appendFunctionFactory();

            for(Optional<A> e : input){
                result=map2(result,e,append);
            }

            return result;
    }


    static <A,B> CompletableFuture<List<B>> traverse(List<CompletableFuture<A>> input, Function<A,B> f) {
        CompletableFuture<List<B>> result=CompletableFuture.supplyAsync(LinkedList::new);

        BiFunction<List<B>,A, List<B>> append = (l,e) -> {
            l.add(f.apply(e));
            return l;
        } ;

        for(CompletableFuture<A> e : input){
            result=map2(result, e,append);
        }

        return result;
    }

    static <A> CompletableFuture<List<A>> sequence(List<CompletableFuture<A>> input){
        return traverse(input,Function.identity());
    }


}
