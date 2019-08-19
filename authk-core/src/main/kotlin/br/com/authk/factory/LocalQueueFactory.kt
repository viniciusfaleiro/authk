package br.com.authk.factory

import net.openhft.chronicle.queue.ExcerptAppender

interface LocalQueueFactory {
    fun getJournalingAppender() : ExcerptAppender
}