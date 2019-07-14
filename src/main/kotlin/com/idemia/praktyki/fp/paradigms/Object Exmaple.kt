package com.idemia.praktyki.fp.paradigms

import kotlin.random.Random

//SIMULA 67
//SMALLTALK
//ACTORS
//naturally evolves toward procedures when used with global databases
interface HitGenerator{
    fun generateHit():Int
}

object RandomHitGenerator : HitGenerator{  //no impl
    override fun generateHit(): Int {
        return Random.nextInt(30)
    }
}

//EXPLAIN INLINE AND THAT LANG LIMITATIONS ARE NO PARADIGM LIMITATIONS
inline class StatusReport(val energy:Int)

class Robot(
        val name:String,
        private var energy:Int,
        private val generator:HitGenerator){

    fun isDead(): Boolean = energy<=0

    fun generateHit() : Int = generator.generateHit()

    //TELL DON'T ASK
    fun takeHit(hit:Int){
        energy -= hit
    }

    fun reportStatus():StatusReport = StatusReport(energy)

}

//Message Broker
class Game{
    class GameResult(val winner:String)

    private val robotInstance1 = Robot("Robot1", 100, RandomHitGenerator)
    private val robotInstance2 = Robot ("Robot2", 100, RandomHitGenerator)

    fun play(): GameResult{
        while (true){
            fight(robotInstance1,robotInstance2)

            if(robotInstance2.isDead())
                return GameResult("robot1")

            fight(robotInstance2,robotInstance1)

            if(robotInstance1.isDead())
                return GameResult("robot2")
        }
    }

    private fun fight(r1:Robot, r2:Robot){
        val robot1Hit=r1.generateHit()
        println("${r1.name}  hit $robot1Hit")
        r2.takeHit(robot1Hit)
        println("${r2.name}  energy is ${r2.reportStatus().energy}")
    }
}

fun main() {
    val game= Game()
    val result= game.play()

    println("and the winnder is : ${result.winner}")

}
