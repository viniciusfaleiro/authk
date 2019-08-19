package br.com.authk.util

import kotlin.properties.Delegates

class TPSLogger (loggerId : String) {
    private var counter : Int = 0
    private var timestamp : Long = System.currentTimeMillis()
    private var printFreqSeconds : Long = 5000
    private var totalMessageCounter : Long = 0
    private val oneSecInMilli = 1000
    private var loggerId : String by Delegates.notNull()

    init {
        this.loggerId = loggerId
    }

    fun event(){
        counter++
        totalMessageCounter++

        val now = System.currentTimeMillis()

        if(now.minus(timestamp) > printFreqSeconds){
            println(loggerId + " TPS:" + (counter.div((printFreqSeconds.div(oneSecInMilli)))) + " Total Msgs:" + totalMessageCounter)

            timestamp = now
            counter = 0
        }
    }
}