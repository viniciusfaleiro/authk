package br.com.authk.data

import br.com.authk.context.Context
import com.lmax.disruptor.EventFactory

class ContextEvent{
    var context : Context? = null
    val eventFactory: EventFactory<ContextEvent> = EventFactory { ContextEvent() }
}