package br.com.authk.factory

import br.com.authk.data.ContextEvent
import com.lmax.disruptor.EventHandler

interface EventHandlerFactory {
    fun preBusinessLogicHandlers() : List<EventHandler<ContextEvent>>

    fun businessHandlers() : List<EventHandler<ContextEvent>>

    fun postBusinessLogicHandlers() : List<EventHandler<ContextEvent>>
}