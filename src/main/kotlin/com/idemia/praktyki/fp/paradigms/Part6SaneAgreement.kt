package com.idemia.praktyki.fp.paradigms

fun main() {
    val initialCup = ImmutableCup(100)

    val cupState2 = initialCup.drink(30)
    val cupToString: (Int) -> String = { state -> "cup state is $state" }

    println("initial cup" + initialCup.receive(cupToString))
    println("after drink" + cupState2.receive(cupToString))

    val cup = CupStructure(100)
    val cup2 = CupStructure(100)

    val drinkFromCup1 = drink(cup)
    println("drink cup 1" + drinkFromCup1(35))

    val drinkFromCup2 = drink2(cup2)
    println("drink cup 2" + drinkFromCup2(45))
}

class ImmutableCup(private val state: Int) {

    fun drink(howMuch: Int): ImmutableCup =
            ImmutableCup(state - howMuch)


    fun <A> receive(message: (Int) -> A): A = message(state)
}


data class CupStructure(val state: Int)

//Currying
val drink: (CupStructure) -> (Int) -> CupStructure = { receiver ->
    { howMuchToDrink ->
        CupStructure(receiver.state - howMuchToDrink)
    }
}

fun drink2(receiver: CupStructure): (Int) -> CupStructure = { howMuchToDrink ->
       CupStructure(receiver.state - howMuchToDrink)
}