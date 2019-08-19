package br.com.authk.factory.auth

import br.com.authk.factory.LocalQueueFactory
import net.openhft.chronicle.queue.ExcerptAppender
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder


class GatewayLocalQueueFactory : LocalQueueFactory {

    override fun getJournalingAppender() : ExcerptAppender {
        val path = "c:/tmp/gw_shared_queue.dat"
        val queue = SingleChronicleQueueBuilder.binary(path).build()
        return queue.acquireAppender()
    }
}