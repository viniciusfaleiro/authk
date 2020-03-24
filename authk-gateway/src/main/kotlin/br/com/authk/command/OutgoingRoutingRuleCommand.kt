package br.com.authk.command

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.CacheKeyPrefix
import br.com.authk.data.ContextKey
import br.com.authk.data.InternalAuthorizationRequest
import br.com.authk.state.ChronicleMapSystemState

/**
 * Best effort to define a good instance with a near cache for a given authorization request
 */
class OutgoingRoutingRuleCommand : Command{
    private val localState = ChronicleMapSystemState.Companion.instance()

    override fun execute(ctx: Context): Boolean {
        val req = ctx.get(ContextKey.OPERATION_REQUEST_ID.toString()) as InternalAuthorizationRequest

        var destinationHost = localState.get(CacheKeyPrefix.CREDIT_CARD_NUMBER_.composeKey(req.getAccountNumber()))

        if(destinationHost == null){
            destinationHost = Configuration.instance().grpcDefaultOutboundHost()
        }

        ctx.save(ContextKey.DESTINATION_HOST.toString(), destinationHost)

        return false;
    }

    override fun isApplicableFor(ctx: Context): Boolean {
        val req = ctx.get(ContextKey.OPERATION_REQUEST_ID.toString()) as InternalAuthorizationRequest?
        return req != null && req.getAccountNumber() != null
    }
}