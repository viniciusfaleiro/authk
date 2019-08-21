package br.com.authk.advice

import br.com.authk.config.Configuration
import br.com.authk.data.StateKeySet
import br.com.authk.factory.AdministrationCommunicationFactory
import br.com.authk.state.ChronicleMapSystemState
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class KeyAffinityAdviceHandler : Runnable {
    private val adminMessageConsumer = AdministrationCommunicationFactory().createAdminConsumer()
    private val jsonMapper = ObjectMapper().apply {
        registerKotlinModule()
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }
    private val localState = ChronicleMapSystemState.instance()

    override fun run() {
        adminMessageConsumer.subscribe(listOf(Configuration.instance().getAdministrationTopic()))

        while (true) {
            val record = adminMessageConsumer.poll(Configuration.instance().getAdministrationConsumerPoolDuration())

            record.iterator().forEach {
                val stateKeySetJson = it.value()

                val stateKeySet = jsonMapper.readValue(stateKeySetJson, StateKeySet::class.java)

                stateKeySet.keys?.iterator()?.forEach {
                    localState.store(it, stateKeySet.hosts.ip)
                }
            }
        }
    }

    fun start() {
        Thread(this).start()
    }
}