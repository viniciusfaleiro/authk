package br.com.authk.command

import br.com.authk.context.Context
import br.com.authk.data.Message
import br.com.authk.data.MessageType
import br.com.authk.data.StateKeySet
import br.com.authk.state.ChronicleMapSystemState

class UpdateAffinityState : Command{
    private val localState = ChronicleMapSystemState.instance()

    override fun isApplicableFor(ctx: Context): Boolean {
        return MessageType.AFFINITY_STATE_KEY_SET.value ==
                (ctx.get(Context.MESSAGE_ID_KEY) as Message).messageType
    }

    override fun execute(ctx: Context): Boolean {
        var msg = (ctx.get(Context.MESSAGE_ID_KEY) as Message)

        var stateKeySet = msg.payload as StateKeySet

        stateKeySet.keys?.iterator()?.forEach {
            localState.store(it, stateKeySet.hosts.ip)
        }

        return false
    }
}