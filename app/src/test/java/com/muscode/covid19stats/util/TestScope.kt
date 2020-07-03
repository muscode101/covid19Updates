//package com.muscode.covid19stats.util
//
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Job
//import kotlin.coroutines.CoroutineContext
//
//class TestScope(override val coroutineContext: CoroutineContext) : ManagedCoroutineScope {
//    val scope = TestCoroutineScope(coroutineContext)
//    override fun launch(block: suspend CoroutineScope.() -> Unit): Job {
//        return scope.launch {
//            block.invoke(this)
//        }
//    }
//}