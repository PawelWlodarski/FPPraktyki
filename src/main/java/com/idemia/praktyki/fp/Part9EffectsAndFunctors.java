package com.idemia.praktyki.fp;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Part9EffectsAndFunctors {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var list = List.of(1,2,3);
        Optional<Integer> result = list.stream().reduce(Integer::sum);

        Function<Integer, String> justAFunction = i -> "result is "+i;

        Optional<String> mappedResult1 = mapExample1(justAFunction, result);

        //brainfuck ---> simple function liften to Effects
        Function<Optional<Integer>, Optional<String>> lifted = mapExample2(justAFunction);

        //Java Limits -  no HK types ,
        Function<Stream<Integer>, Stream<String>> liftedToStreams = liftStream(justAFunction);
        Stream<String> mappedStream = liftedToStreams.apply(list.stream());

        //fpAsyncProgramExample(justAFunction);

        //ArrowKT
        //flatMap ??
    }




    static <A,B> Optional<B> mapExample1(Function<A,B> f, Optional<A> receiver){
        return receiver.map(f);
    }

    //Currying
    static <A,B>  Function<Optional<A>,Optional<B>> mapExample2(Function<A,B> f){
        return receiver -> receiver.map(f);
    }


    //stream lift
    static <A,B>  Function<Stream<A>,Stream<B>> liftStream(Function<A,B> f){
        return receiver -> receiver.map(f);
    }

    //stream Future
    static <A,B>  Function<CompletableFuture<A>,CompletableFuture<B>> liftFuture(Function<A,B> f){
        return receiver -> receiver.thenApply(f);  //thenApply is just 'map'
    }

    //Java Limits -  no HK types ,
    // fun lift<A, M<_>>(f:(A) -> b, implicit Functor<M> functor) : (M<A>) -> M<B>{
    //    return m -> functor.map(m)(f)
    // }


    private static void fpAsyncProgramExample(Function<Integer, String> justAFunction) throws InterruptedException, ExecutionException {
        /**
         *!!!!From simple functions (unit tests layer) to system effects (integration tests layer)!!!!
         */
        //TEST UNIT LAYER - no Future, no optional, no streams
        Supplier<Integer> generator = () -> 5;
        UnaryOperator<String> businessFunction = String::toUpperCase;
        Consumer<String> sideEffectFunction = System.out::println;

        //Integration test layer -- async effect example
        Function<Supplier<Integer>, CompletableFuture<Integer>> unit = CompletableFuture::supplyAsync;
        Function<CompletableFuture<Integer>, CompletableFuture<String>> businnesEffect1 = liftFuture(justAFunction);
        Function<CompletableFuture<String>, CompletableFuture<String>> businnesEffect2 = liftFuture(businessFunction);
        Function<CompletableFuture<String>, CompletableFuture<String>> addPrefix = liftFuture(s -> "ASYNC "+s );
        Function<CompletableFuture<String>, CompletableFuture<Void>> edgeOfTheSystem = f -> f.thenAccept(sideEffectFunction);

        //FPProgram - docencie ze napisałem to w tak gównianym języku jak java
        Function<Supplier<Integer>, CompletableFuture<Void>> fpAsyncProgram = unit
                        .andThen(businnesEffect1)
                        .andThen(businnesEffect2)
                        .andThen(addPrefix)
                        .andThen(edgeOfTheSystem);

        CompletableFuture<Void> terminationSignal = fpAsyncProgram.apply(generator);

        terminationSignal.get(); //await end
    }
}
