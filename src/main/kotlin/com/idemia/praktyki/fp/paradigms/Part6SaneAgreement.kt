package com.idemia.praktyki.fp.paradigms

fun main() {
    val initialCup = ImmutableCup(100)

    val cupState2 = initialCup.drink(30)
    val cupToString : (Int) -> String = {state -> "cup state is $state"}

    println("initial cup" + initialCup.receive(cupToString))
    println("after drink" + cupState2.receive(cupToString))

}

class ImmutableCup(private val state:Int){

    fun drink(howMuch:Int) : ImmutableCup =
            ImmutableCup(state - howMuch)


    fun <A> receive(message:(Int) -> A) : A = message(state)

}