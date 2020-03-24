package br.com.authk.server

import br.com.authk.advice.KeyAffinityAdvisor
import br.com.authk.config.AuthConfiguration
import br.com.authk.config.Configuration
import br.com.authk.listener.AuthGRPCMessageListener
import br.com.authk.state.HazelCastMapSystemState

fun main() {
    //start cache
    HazelCastMapSystemState.instance()

    //start GRPC Server for incoming messages
    Configuration.config(AuthConfiguration())
    AuthGRPCMessageListener().listen()



    //KeyAffinityAdvisor().start()
}
