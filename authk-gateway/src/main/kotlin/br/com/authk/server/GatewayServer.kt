package br.com.authk.server

import br.com.authk.config.Configuration
import br.com.authk.config.GatewayConfiguration
import br.com.authk.listener.GatewayGPRCMessageListener
import br.com.authk.listener.KeyAffinityAdviceListener
import br.com.authk.listener.MockISOSocketMessageListener
import org.jpos.iso.IFAE_LLCHAR
import org.jpos.iso.IFA_FLLNUM
import org.jpos.iso.IFA_LLNUM

fun main() {
    //start GRPC Server for message responses
    Configuration.config(GatewayConfiguration())
    //GatewayGPRCMessageListener().listen()
    //KeyAffinityAdviceListener().listen()

    val inboundProcessor = MockISOSocketMessageListener()

    val payload = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR031VETEALDEMONIOISO8583 1234567890"
    val payloadBin = "0110762020810ED0A602104358720001137956000000000000005900000000001537080522184228575800760006435036F9F2F1F7F2F2F2F8F5F7F5F8F8F2F2F9F1F4F0F0F0F6F3F7F5F2F6F2F0F0F0F0F0F0F0F0F4F4F3F6F7F9F30F4040404040404040404040404040400986084014F0F0F0F1F9F8F6C3F0F0F0F0F0F0F0F5F1F5F2F50F01000C910AB232A9A4F3B979443030058000000002"
                     "---!---------------!16---------------!-----!-----------!"

    val totalMessages = 100_000

    for (i in 1..totalMessages) {
        inboundProcessor.listen(payloadBin)
        Thread.sleep(1000)
    }
}
