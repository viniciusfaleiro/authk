package br.com.authk.advice

import br.com.authk.config.Configuration
import br.com.authk.data.AffinityHost
import br.com.authk.data.CacheKeyPrefix
import br.com.authk.data.StateKeySet
import br.com.authk.factory.AdministrationCommunicationFactory
import br.com.authk.state.HazelCastMapSystemState
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.producer.ProducerRecord
import java.net.InetAddress

class KeyAffinityAdvisor () : Runnable {
    private val adviceFrequency = Configuration.instance().keyAdviseFrequency()
    private val adminMessageProducer = AdministrationCommunicationFactory().createAdminProducer()
    private val jsonMapper = ObjectMapper().apply {
        registerKotlinModule()
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        dateFormat = StdDateFormat()
    }

    override fun run() {
        while(true) {
            var cacheKeySet: Set<String> = HazelCastMapSystemState.instance().keySet()

            var cacheCreditCardNumber = HashSet(cacheKeySet
                .filter { key -> key.startsWith(CacheKeyPrefix.CREDIT_CARD_NUMBER_.toString()) })

            var stateKeySet = StateKeySet(AffinityHost(InetAddress.getLocalHost().hostName), cacheCreditCardNumber)

            adminMessageProducer.send(ProducerRecord(Configuration.instance().getAdministrationTopic(), jsonMapper.writeValueAsString(stateKeySet)))

            Thread.sleep(adviceFrequency)
        }
    }

    fun start(){
        Thread(this).start()
    }

}