package com.scarlet.coroutines.advanced

import com.scarlet.util.onCompletion
import kotlinx.coroutines.*

object Canceling_Parent_Cancels_All_Children {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking{

        // What if change to Job()
        val scope = CoroutineScope(SupervisorJob())

        val child1 = scope.launch {
            println("child1 started")
            delay(1000)
            println("child1 done")
        }.onCompletion("child 1")

        val child2 = scope.launch {
            println("child2 started")
            delay(1000)
            println("child2 done")
        }.onCompletion("child 2")

        delay(500)

        scope.cancel()
        joinAll(child1, child2)

        println("is Parent scope cancelled? = ${TODO()}")
    }
}

object Canceling_A_Child_Cancels_Only_The_Target_Child_Including_All_Its_Descendents_If_Any {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking{

        // What if change to Job()
        val scope = CoroutineScope(SupervisorJob())

        val child1 = scope.launch {
            println("child1 started")
            delay(1000)
            println("child1 done")
        }.onCompletion("child 1")

        val child2 = scope.launch {
            println("child2 started")
            delay(1000)
            println("child2 done")
        }.onCompletion("child 2")

        delay(500)

        child1.cancel()
        joinAll(child1, child2)

        println("is Parent cancelled? = ${scope.coroutineContext[Job]?.isCancelled}")
    }
}

object SupervisorJob_Child_Failure_SimpleDemo {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking{

        // What if change to Job()
        val scope = CoroutineScope(SupervisorJob())

        val child1 = scope.launch {
            println("child1 started")
            delay(500)
            throw RuntimeException("child 1 failed")
        }.onCompletion("child 1")

        val child2 = scope.launch {
            println("child2 started")
            delay(1000)
            println("child2 done")
        }.onCompletion("child 2")

        joinAll(child1, child2)

        println("is Parent cancelled? = ${scope.coroutineContext[Job]?.isCancelled}")
    }
}