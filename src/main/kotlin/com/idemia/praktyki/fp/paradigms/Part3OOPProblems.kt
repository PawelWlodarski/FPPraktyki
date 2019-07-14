package com.idemia.praktyki.fp.paradigms

fun main() {
//    saneWorld()
//    worldOfMadness()
    madnessMultiplied()
}


fun saneWorld(){
    println("2+2=${2+2}")
    println("2+3=${2+3}")
    println("3+2=${3+2}")
}

//lets do some OOP programming!!!
class OOPNumber(private var value:Int){
    operator fun plus(other:OOPNumber):OOPNumber{
        value += other.value
        return this
    }

    override fun toString(): String {
        return "OOPNumber(value=$value)"
    }
}

val TWO = OOPNumber(2)
val THREE = OOPNumber(3)

//change order
fun worldOfMadness(){
    println("OOP 2+2=${TWO + TWO}")
    println("OOP 2+3=${TWO + THREE}")
    println("OOP 3+2=${THREE + TWO}")
    println("and again OOP 2+2=${TWO + TWO}")
}

fun madnessMultiplied(){

    repeat(30){
        val ONE = OOPNumber(1)
        val oopNumbers=(1 .. 20).map { ONE }.toList()
        val result = oopNumbers
                .parallelStream()
                .reduce(OOPNumber::plus)

        println(result)
    }

}
