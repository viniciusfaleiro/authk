package br.com.authk.ring

import br.com.authk.context.Context
import br.com.authk.factory.RingID

interface MessageRingBufferPublisher {
    fun publish(ctx : Context, ringID : RingID)
}