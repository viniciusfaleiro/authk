package br.com.authk.listener

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.Message
import br.com.authk.data.MessageType
import br.com.authk.data.StateKeySet
import br.com.authk.factory.AdministrationCommunicationFactory
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import br.com.authk.sequence.StatefullSequencer
import br.com.authk.state.ChronicleMapSystemState
import br.com.authk.util.TPSLogger
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.lang.management.ManagementFactory
import java.net.InetAddress
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class KeyAffinityAdviceListener : Runnable, Listener {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)
    private var publisher: RingPublisher = RingPublisher()
    private val sequencer = StatefullSequencer()
    private val hostName =  InetAddress.getLocalHost().hostName
    private val runtimeId = ManagementFactory.getRuntimeMXBean().name
    private val adminMessageConsumer = AdministrationCommunicationFactory().createAdminConsumer()
    private val jsonMapper = ObjectMapper().apply {
        registerKotlinModule()
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }

    override fun listen() {
        Thread(this).start()
    }

    override fun run() {
        adminMessageConsumer.subscribe(listOf(Configuration.instance().getAdministrationTopic()))

        while (true) {
            tpsLogger.event()

            val record = adminMessageConsumer.poll(Configuration.instance().getAdministrationConsumerPoolDuration())

            record.iterator().forEach {
                val stateKeySetJson = it.value()

                val stateKeySet = jsonMapper.readValue(stateKeySetJson, StateKeySet::class.java)

                delegate(listOf(Message(
                    MessageType.AFFINITY_STATE_KEY_SET.value,
                    hostName,
                    null,
                    stateKeySet,
                    sequencer.next(),
                    UUID.randomUUID(),
                    null,
                    runtimeId,
                    ZonedDateTime.now(ZoneId.of("UTC")))))
            }
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