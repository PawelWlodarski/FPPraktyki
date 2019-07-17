package com.idemia.praktyki.fp.exercise;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.idemia.praktyki.fp.exercise.EffectsLibrary.map2;


public class JavaExercises {


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
        BiFunction<Integer,Integer,Integer> add = (i, j) -> i+j;
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

    @Test
    public void ex4FPvsExceptions(){

        String result = Try.of(provideContent("file1"))
                .recover(e -> Try.of(provideContent("file2")))
                .flatMap(name -> Try.of(provideContent(name)))
                .fold(c -> "result : "+c, Throwable::getMessage);

        Assertions.assertThat(result).isEqualTo("result : final content");
    }

    private Supplier<String> provideContent(String fileName){
        return  () -> {
            //java...
            try {
                return readFile(fileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
    }

    //example with checked exception which destroys lambdas
    private String readFile(String name) throws FileNotFoundException {
        switch (name){
            case "file2" :
                return "anotherName";
            case "anotherName" :
                return "final content" ;
            default:
                throw new FileNotFoundException();
        }
    }


    @Test
    public void ex5Map2(){
        Optional<Integer> optionalResult = map2(Optional.of(1), Optional.of(2), Integer::sum);

        CompletableFuture<Integer> asyncResult1 = map2(
                CompletableFuture.supplyAsync(() -> 1), CompletableFuture.completedFuture(2), Integer::sum
        );

        CompletableFuture<Integer> asyncResult2 =
                map2(asyncResult1, CompletableFuture.supplyAsync(() -> 4), (i1,i2) -> i1*i2);

        Assertions.assertThat(optionalResult).contains(3);
        Assertions.assertThat(asyncResult2).isCompletedWithValue(12);
    }

    @Test
    public void ex5FPBossSequence(){
        List<Optional<Integer>> optionals = List.of(Optional.of(1), Optional.of(2), Optional.of(3));
        List<Optional<Integer>> optionalsWithEmpty = List.of(Optional.of(1), Optional.empty(), Optional.of(3));

        Optional<List<Integer>> result = EffectsLibrary.sequence(optionals);
        Optional<List<Integer>> resultWithEmpty = EffectsLibrary.sequence(optionalsWithEmpty);

        Assertions.assertThat(result).contains(List.of(1,2,3));
        Assertions.assertThat(resultWithEmpty).isEmpty();
    }


    @Test
    public void ex5FPBossTraverse() throws ExecutionException, InterruptedException {
        List<CompletableFuture<String>> futures = List.of("aa", "bbb", "cccc").stream()
                .map(i -> CompletableFuture.supplyAsync(() -> i))
                .collect(Collectors.toList());

        CompletableFuture<List<Integer>> result = EffectsLibrary.traverse(futures, String::length);


        List<Integer> justAList = result.get();

        Assertions.assertThat(justAList).isEqualTo(List.of(2,3,4)) ;
    }
}


class Robot{

    final private int energy;

    private Robot(int energy){
        this.energy = energy;
    }

    //EXERCISE
    static Robot buildRobot(int energy){
        return null;
    }

    //EXERCISE
    Robot modify(IntUnaryOperator f){
        return  null;
    }

    //EXERCISE
    <A> A get(IntFunction<A> c){
        return null;
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
