package br.com.authk.processor

import br.com.authk.config.Configuration
import br.com.authk.context.Context
import br.com.authk.data.Message
import br.com.authk.data.ContextEvent
import br.com.authk.factory.RingID
import br.com.authk.ring.RingPublisher
import br.com.authk.util.TPSLogger
import com.lmax.disruptor.EventHandler
import net.openhft.chronicle.queue.ExcerptAppender

class JournalerProcessor : EventHandler<ContextEvent>, Delegatable {
    private val tpsLogger = TPSLogger(this.javaClass.canonicalName)
    private var publisher : RingPublisher = RingPublisher()

    private var journalAppender : ExcerptAppender = Configuration.instance()
        .localQueueFactory()
        .getJournalingAppender()

    override fun onEvent(event: ContextEvent?, sequence: Long, wtv: Boolean) {
        val ctx = event!!.context
        val msg = ctx!!.get(Context.MESSAGE_ID_KEY) as Message

        journalAppender.writeText(msg!!.rawMessage!!)

        delegate(ctx)
    }

    override fun delegate(ctx : Context){
        publisher.publish(ctx, RingID.PRE_BUSINESS_TO_BUSINESS)
    }
}