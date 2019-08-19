package br.com.authk.processor

import br.com.authk.command.Command
import br.com.authk.context.Context
import br.com.authk.data.ContextEvent
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import br.com.authk.util.TPSLogger
import com.lmax.disruptor.EventHandler


class CommandChainProcessor : EventHandler<ContextEvent>, Delegatable {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)
    private var publisher = RingPublisher()
    private val commands: MutableList<Command> = mutableListOf()

    override fun onEvent(event: ContextEvent?, sequence: Long, wtv: Boolean) {
        tpsLogger.event()

        val ctx = event!!.context

        for (cmd in commands) {
            if (ctx != null && cmd.isApplicableFor(ctx)) {
                if(cmd.execute(ctx)){
                    break;
                }
            }
        }

        delegate(ctx!!)
    }

    override fun delegate(ctx: Context) {
        publisher.publish(ctx, RingID.BUSINESS_TO_OUTBOUND)
    }

    fun addCommand(cmd: Command): CommandChainProcessor {
        commands.add(cmd)
        return this
    }

}