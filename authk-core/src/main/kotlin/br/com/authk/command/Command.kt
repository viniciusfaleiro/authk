package br.com.authk.command

import br.com.authk.context.Context

interface Command {
    fun isApplicableFor(ctx : Context) : Boolean
    fun execute (ctx : Context) : Boolean
}