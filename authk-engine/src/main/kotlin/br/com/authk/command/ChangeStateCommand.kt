package br.com.authk.command

import br.com.authk.context.Context
import br.com.authk.state.HazelCastMapSystemState

class ChangeStateCommand : Command {
    private val state = HazelCastMapSystemState.Companion.instance()

    override fun execute(ctx: Context): Boolean {
        state.store("TIMESTAMP", System.currentTimeMillis().toString())

        return false;
    }

    override fun isApplicableFor(ctx: Context): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}