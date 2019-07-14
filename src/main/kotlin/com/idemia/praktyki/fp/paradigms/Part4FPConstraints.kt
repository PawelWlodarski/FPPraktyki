package com.idemia.praktyki.fp.paradigms

import kotlin.random.Random

fun main() {
    //#1 - function must be pure
    pureFunctions()

    //#2 - no "observable" mutability
    //var a=1 //not allowed
    //val l= mutableListOf<Int>() //not allowed

    //#3 - every function has domain and codomain - UNIT is Java void
    //val sideEffectFunction: (String) -> Unit = {i -> println(i)} //not allowed
    val result = if(Random.nextBoolean()) 1 else 2 // if is an expression not a statement

    //#4 - Referential transparency
    rtExample()

}

private fun pureFunctions() {
    val pureFunction = { i: Int -> i + 1 }
    repeat(10) {
        println("pure(1) : " + pureFunction(1))
    }

    val sideEffect = mutableListOf(1)
    val impureFunction = { i: Int -> sideEffect.sum() + i }
    repeat(10) {
        sideEffect.add(1)
        println("impure(1) : " + impureFunction(1))
    }
}

fun rtExample() {
    fun pureFunction(i:Int)= i+1

    val r1 = pureFunction(1)

    //you can replace result with function call
    assert(r1+r1 == r1 + pureFunction(1))


    val sideEffect = mutableListOf(1)
    val impureFunction = { i: Int ->
        sideEffect.add(i)
        sideEffect.sum() + i
    }
    val r2 = impureFunction(1)

    assert(r2+r2 != r2 + impureFunction(1))
}
