package br.com.authk.processor

import br.com.authk.context.Context
import br.com.authk.data.ContextEvent
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import com.lmax.disruptor.EventHandler

class BypassProcessor (destination : RingID) : EventHandler<ContextEvent>, Delegatable {
    private val publisher : RingPublisher = RingPublisher()
    private val destination = destination

    override fun onEvent(event: ContextEvent?, sequence: Long, wtv: Boolean) {
        delegate(event!!.context!!)
    }

    override fun delegate(ctx : Context){
        publisher.publish(ctx, destination)
    }
}