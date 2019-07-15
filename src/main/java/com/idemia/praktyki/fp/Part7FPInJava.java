package com.idemia.praktyki.fp;

import java.util.function.Function;

public class Part7FPInJava {

    public static void main(String[] args) {
        var cup = new CupStructure(100);

        Function<Integer,CupStructure> drinkFromCup1 = drink(cup);
        System.out.println("drink cup 1 : " + drinkFromCup1.apply(35));


        var stringBox=Box.unit("helllo");
        Box<Integer> intBox = stringBox.map(String::length);
        System.out.println(intBox);
    }


    static Function<Integer,CupStructure> drink(CupStructure receiver){
        return howMuchToDrink -> new CupStructure(receiver.amount - howMuchToDrink);
    }

    static class CupStructure{
        final int amount;

        @Override
        public String toString() {
            return "CupStructure{" +
                    "amount=" + amount +
                    '}';
        }

        CupStructure(int amount) {
            this.amount = amount;
        }
    }

    static class Box<A>{
        private final A somethingInTheBox;

        static <A> Box<A> unit(A a){
            return new Box<>(a);
        }

        private Box(A somethingInTheBox) {
            this.somethingInTheBox = somethingInTheBox;
        }

        <B> Box<B> map(Function<A,B> f){
            return Box.unit(f.apply(somethingInTheBox));
        }

        @Override
        public String toString() {
            return "Box{" +
                    "somethingInTheBox=" + somethingInTheBox +
                    '}';
        }
    }
}
