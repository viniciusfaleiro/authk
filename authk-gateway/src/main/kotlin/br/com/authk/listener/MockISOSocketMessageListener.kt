package br.com.authk.listener

import br.com.authk.context.Context
import br.com.authk.data.Message
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import br.com.authk.sequence.StatefullSequencer
import br.com.authk.util.TPSLogger
import java.lang.management.ManagementFactory
import java.nio.charset.StandardCharsets
import org.jpos.iso.packager.GenericPackager
import org.jpos.iso.ISOMsg
import java.net.InetAddress
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class MockISOSocketMessageListener {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)
    private var publisher: RingPublisher = RingPublisher()
    private val sequencer = StatefullSequencer()
    private val hostName =  InetAddress.getLocalHost().hostName
    private val runtimeId = ManagementFactory.getRuntimeMXBean().name

    fun listen(rawMessage: String) {
       tpsLogger.event()

        if(rawMessage != null) {
            delegate(listOf(Message(
                "0110",
                hostName,
                rawMessage,
                null,
                sequencer.next(),
                UUID.randomUUID(),
                null,
                runtimeId,
                ZonedDateTime.now(ZoneId.of("UTC")))))
        }
    }

    private fun delegate(responses: List<Message>) {
        for (msg in responses) {
            val ctx = Context()
            ctx.save(Context.MESSAGE_ID_KEY, msg)

            publisher.publish(ctx, RingID.INBOUND_TO_PRE_BUSINESS)
        }
    }
}