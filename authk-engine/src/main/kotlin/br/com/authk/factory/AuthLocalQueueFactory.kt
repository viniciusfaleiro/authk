package br.com.authk.factory

import br.com.authk.factory.LocalQueueFactory
import net.openhft.chronicle.queue.ExcerptAppender
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue

class AuthLocalQueueFactory : LocalQueueFactory {

    override fun getJournalingAppender() : ExcerptAppender {
        val path = "c:/tmp/auth_shared_queue.dat"
        val queue = SingleChronicleQueueBuilder.binary(path).build()
        return queue.acquireAppender()
    }
}