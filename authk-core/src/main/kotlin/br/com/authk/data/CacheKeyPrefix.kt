package br.com.authk.data

enum class CacheKeyPrefix {
    CREDIT_CARD_NUMBER_;

    fun composeKey(value : String) : String{
        return this.toString() + value;
    }
}