package br.com.authk.server

import br.com.authk.config.AuthConfiguration
import br.com.authk.config.Configuration
import br.com.authk.listener.AuthGRPCMessageListener

fun main() {
    //start GRPC Server for incoming messages
    Configuration.config(AuthConfiguration())
    AuthGRPCMessageListener().listen()
}
