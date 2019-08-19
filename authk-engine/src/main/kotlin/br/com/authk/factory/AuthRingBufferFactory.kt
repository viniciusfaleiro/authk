package br.com.authk.factory

import br.com.authk.data.ContextEvent
import br.com.authk.factory.RingBufferFactory
import br.com.authk.factory.RingID
import com.lmax.disruptor.BusySpinWaitStrategy
import com.lmax.disruptor.EventHandler
import com.lmax.disruptor.RingBuffer
import com.lmax.disruptor.dsl.Disruptor
import com.lmax.disruptor.dsl.ProducerType
import java.lang.RuntimeException
import java.util.concurrent.Executors

class AuthRingBufferFactory : RingBufferFactory {
    private val ringBufferSize = 2048;
    private val processors = 1;

    companion object {
        val RING_ID_BUFFER_CACHE : MutableMap<RingID, RingBuffer<ContextEvent>> = mutableMapOf()
        val EVENT_HANDLERS_BY_RING_ID : MutableMap<RingID, List<EventHandler<ContextEvent>>> = mutableMapOf()

        init{
            EVENT_HANDLERS_BY_RING_ID[RingID.INBOUND_TO_PRE_BUSINESS] = AuthEventHandlerFactory()
                .preBusinessLogicHandlers()
            EVENT_HANDLERS_BY_RING_ID[RingID.PRE_BUSINESS_TO_BUSINESS] = AuthEventHandlerFactory()
                .businessHandlers()
            EVENT_HANDLERS_BY_RING_ID[RingID.BUSINESS_TO_OUTBOUND] = AuthEventHandlerFactory()
                .postBusinessLogicHandlers()
        }
    }

    private fun createDisruptor (eventHandlers: List<EventHandler<ContextEvent>>) : Disruptor<ContextEvent>{
        var disruptor: Disruptor<ContextEvent> = Disruptor(ContextEvent().eventFactory,
            ringBufferSize,
            Executors.newFixedThreadPool(processors),
            ProducerType.SINGLE,
            BusySpinWaitStrategy())

        for(eventHandler in eventHandlers) {
            disruptor.handleEventsWith(eventHandler)
        }

        return disruptor;
    }

    override fun lazyGetRingBuffer(ringID : RingID) : RingBuffer<ContextEvent>?{
        var ringBuffer = RING_ID_BUFFER_CACHE[ringID]

        if(ringBuffer == null) {
            val handlers = EVENT_HANDLERS_BY_RING_ID[ringID] ?: throw RuntimeException("Unknown handler for ring $ringID")

            val disruptor: Disruptor<ContextEvent> =
                AuthRingBufferFactory().createDisruptor(handlers)
            ringBuffer = disruptor.start()

            RING_ID_BUFFER_CACHE[ringID] = ringBuffer
        }

        return ringBuffer
    }
}