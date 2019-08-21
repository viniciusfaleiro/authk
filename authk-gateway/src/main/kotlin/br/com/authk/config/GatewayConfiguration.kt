package br.com.authk.config

import br.com.authk.factory.LocalQueueFactory
import br.com.authk.factory.RingBufferFactory
import br.com.authk.factory.auth.GatewayLocalQueueFactory
import br.com.authk.factory.auth.GatewayRingBufferFactory
import java.time.Duration

class GatewayConfiguration : Configuration() {
    override fun grpcServerPort(): Int {
        return 9002
    }

    override fun grpcDefaultOutboundHost(): String {
        return "localhost"
    }

    override fun grpcDefaultOutboundPort(): Int {
        return 9001
    }

    override fun localQueueFactory(): LocalQueueFactory {
        return GatewayLocalQueueFactory()
    }

    override fun ringBufferFactory(): RingBufferFactory {
        return GatewayRingBufferFactory()
    }

    override fun adviseKeySet(): Boolean {
        return false
    }

    override fun keyAdviseFrequency(): Long {
        return 0;
    }

    override fun getAdministrationBrokerList(): String {
        return "localhost:9092"
    }

    override fun getAdministrationConsumerPoolDuration(): Duration {
        return Duration.ofSeconds(1L)
    }

    override fun getAdministrationTopic(): String {
        return "authorization_admin_topic"
    }
}