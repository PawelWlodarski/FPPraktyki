package com.idemia.praktyki.fp.paradigms

import kotlin.random.Random


//A.K.A DATABASE
class RobotDataStructure(var energy:Int)

var robot1 = RobotDataStructure( 100)
var robot2 = RobotDataStructure( 100)

fun fightProcedure(){

    while(true){
        val robot1HitEnergy= generateHitEnergy()
        println("robot 1 hit energy : $robot1HitEnergy")
        val robot2Energy=hitRobot2(robot1HitEnergy)
        println("robot 2 energy after hit : $robot2Energy")
        if(robot2Energy<0){
            println("robot 1 WON!!!")
            break
        }


        val robot2HitEnergy= generateHitEnergy()
        println("robot 2 hit energy : $robot2HitEnergy")
        val robot1Energy=hitRobot1(robot2HitEnergy)
        println("robot 1 energy after hit : $robot1Energy")
        if(robot1Energy<0){
            println("robot 2 WON!!!")
            break
        }
    }
}

private fun generateHitEnergy():Int{
    return Random.nextInt(30)
}

private fun hitRobot1(hitEnergy:Int):Int{
    robot1.energy -= hitEnergy
    return robot1.energy
}

private fun hitRobot2(hitEnergy:Int):Int{
    robot2.energy -= hitEnergy
    return robot2.energy
}

fun main() {
    fightProcedure()
}