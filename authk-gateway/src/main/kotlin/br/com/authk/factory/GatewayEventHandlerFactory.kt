package br.com.authk.factory.auth

import br.com.authk.command.ConvertISOAuthorizationRequestCommand
import br.com.authk.command.OutgoingRoutingRuleCommand
import br.com.authk.data.ContextEvent
import br.com.authk.factory.EventHandlerFactory
import br.com.authk.factory.RingID
import br.com.authk.processor.AuthorizationRequestOutboundMessageProcessor
import br.com.authk.processor.BypassProcessor
import br.com.authk.processor.CommandChainProcessor
import com.lmax.disruptor.EventHandler

class GatewayEventHandlerFactory: EventHandlerFactory {
    override fun preBusinessLogicHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(CommandChainProcessor()
            .addCommand(ConvertISOAuthorizationRequestCommand()) // Open ISO message
            .addCommand(OutgoingRoutingRuleCommand()) // Define Request Routing
        )

        return handlers
    }

    override fun businessHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(BypassProcessor(RingID.BUSINESS_TO_OUTBOUND))

        return handlers
    }

    override fun postBusinessLogicHandlers() : List<EventHandler<ContextEvent>> {
        val handlers = ArrayList<EventHandler<ContextEvent>>()

        handlers.add(AuthorizationRequestOutboundMessageProcessor())

        return handlers
    }
}