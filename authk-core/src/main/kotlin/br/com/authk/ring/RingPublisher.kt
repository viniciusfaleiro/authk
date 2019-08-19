package br.com.authk.ring

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.factory.RingBufferFactory
import br.com.authk.factory.RingID
import java.lang.RuntimeException

class RingPublisher : MessageRingBufferPublisher {
    private val ringBufferFactory : RingBufferFactory =
        Configuration.instance().ringBufferFactory()

    override fun publish(ctx: Context, ringID: RingID) {
        val ringBuffer = ringBufferFactory.lazyGetRingBuffer(ringID) ?: throw RuntimeException("RingBuffer cannot be null.")

        val sequence = ringBuffer.next()
        val event = ringBuffer.get(sequence)

        event.context = ctx

        ringBuffer.publish(sequence)
    }
}