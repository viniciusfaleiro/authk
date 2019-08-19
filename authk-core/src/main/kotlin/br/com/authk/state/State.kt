package br.com.authk.state

import br.com.authk.data.AffinityHost

interface State {
    fun get(key : String) : Any?
    fun store(key :String, value : String)
    fun keyHasLocalAffinity(key : String) : Boolean
    fun getAffinityHost(key : String) : AffinityHost?
    fun keySet() : Set<String>
}