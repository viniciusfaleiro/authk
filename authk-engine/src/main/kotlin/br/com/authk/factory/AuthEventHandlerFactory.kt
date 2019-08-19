package br.com.authk.factory

import br.com.authk.command.ChangeStateCommand
import br.com.authk.processor.JournalerProcessor
import br.com.authk.data.ContextEvent
import br.com.authk.processor.AuthGRPCOutboundMessageProcessor
import br.com.authk.processor.CommandChainProcessor
import com.lmax.disruptor.EventHandler

class AuthEventHandlerFactory : EventHandlerFactory {
    override fun preBusinessLogicHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(JournalerProcessor())

        return handlers
    }

    override fun businessHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(CommandChainProcessor().addCommand(ChangeStateCommand()))

        return handlers
    }

    override fun postBusinessLogicHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(AuthGRPCOutboundMessageProcessor())

        return handlers
    }
}