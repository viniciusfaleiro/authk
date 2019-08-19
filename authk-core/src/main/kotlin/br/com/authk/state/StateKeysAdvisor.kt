package br.com.authk.state

import br.com.authk.config.Configuration
import br.com.authk.data.AffinityHost
import br.com.authk.data.StateKeySet
import java.net.InetAddress
import kotlin.concurrent.thread

class StateKeysAdvisor {
    private val state = HazelCastMapSystemState.instance()

    fun startAdvisingKeys(){
        if(Configuration.instance().adviseKeySet()){
            thread (start = true){
                val keySet = state.keySet();

                val keyState = StateKeySet(AffinityHost(InetAddress.getLocalHost().hostName), keySet)

                println("Publishing local key:$keySet")
                Thread.sleep(Configuration.instance().keyAdviseFrequency())
            }
        }

    }
}