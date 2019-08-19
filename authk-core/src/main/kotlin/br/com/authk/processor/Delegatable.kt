package br.com.authk.processor

import br.com.authk.context.Context

interface Delegatable {
    fun delegate(ctx : Context)
}