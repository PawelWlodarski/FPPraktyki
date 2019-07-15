package com.idemia.praktyki.fp.paradigms

fun main() {

    val fullCup = cupConstructor(100)

    println("first state : " + getState(fullCup))
    val cupSecondState = drink(fullCup, 30)
    println("first state after modification : " + getState(fullCup))
    println("second state : "+getState(cupSecondState))


}

typealias Message = (Int) -> Int
typealias Cup = (Message) -> Int

fun cupConstructor(state:Int): Cup = {message -> message(state)}

fun getState(cup: Cup) : Int = cup { state:Int -> state}
fun drink(cup: Cup,stateToDrink:Int) : Cup =
        cupConstructor(getState(cup) - stateToDrink)