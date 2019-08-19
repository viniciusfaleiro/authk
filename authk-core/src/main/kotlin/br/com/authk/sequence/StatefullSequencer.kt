package br.com.authk.sequence

import java.util.concurrent.atomic.AtomicLong

class StatefullSequencer : Sequencer {
    var sequence = AtomicLong(0L);

    override fun next(): Long {
        return sequence.getAndAdd(1L);
    }
}