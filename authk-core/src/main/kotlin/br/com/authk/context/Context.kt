package br.com.authk.context

class Context {
    private val context : MutableMap<String, Any> = mutableMapOf()

    companion object {
        val MESSAGE_ID_KEY = "MESSAGE_ID"
    }

    fun save(id : String, value : Any){
        context[id] = value
    }

    fun get(id : String) : Any? {
        return context[id]
    }
}