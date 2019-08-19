package br.com.authk.factory

import br.com.authk.data.ContextEvent
import com.lmax.disruptor.RingBuffer

interface RingBufferFactory {
    fun lazyGetRingBuffer(ringID : RingID) : RingBuffer<ContextEvent>?
}