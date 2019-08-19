package br.com.authk.server

import br.com.authk.config.Configuration
import br.com.authk.config.GatewayConfiguration
import br.com.authk.listener.GatewayGPRCMessageListener
import br.com.authk.listener.MockISOSocketMessageListener

fun main() {
    //start GRPC Server for message responses
    Configuration.config(GatewayConfiguration())
    GatewayGPRCMessageListener().listen()

    val inboundProcessor = MockISOSocketMessageListener()

    val payload = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR031VETEALDEMONIOISO8583 1234567890"

    val totalMessages = 500_000_000

    for (i in 1..totalMessages) {
        inboundProcessor.listen(payload)
    }
}
