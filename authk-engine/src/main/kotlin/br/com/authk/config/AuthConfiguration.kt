package br.com.authk.config

import br.com.authk.factory.LocalQueueFactory
import br.com.authk.factory.RingBufferFactory
import br.com.authk.factory.AuthLocalQueueFactory
import br.com.authk.factory.AuthRingBufferFactory

class AuthConfiguration : Configuration() {
    override fun grpcServerPort(): Int {
        return 9001
    }

    override fun grpcDefaultOutboundHost(): String {
        return "localhost"
    }

    override fun grpcDefaultOutboundPort(): Int {
        return 9002
    }

    override fun localQueueFactory(): LocalQueueFactory {
        return AuthLocalQueueFactory()
    }

    override fun ringBufferFactory(): RingBufferFactory {
        return AuthRingBufferFactory()
    }

    override fun adviseKeySet(): Boolean {
        return true
    }

    override fun keyAdviseFrequency(): Long {
        return 1_000 * 30
    }
}