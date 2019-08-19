package br.com.authk.data

import br.com.authk.rpc.authorization.AuthorizationRequest
import org.jpos.iso.ISOMsg
import java.math.BigDecimal

class InternalAuthorizationRequest(isoMsg : ISOMsg) {
    val isoMessage = isoMsg

    private val BIN_POS = 3
    private val CARD_NUMBER_POS = 4
    private val TRANSACTION_AMMOUNT_POS = 7

    fun getCardBin() : String {
        return isoMessage.getString(BIN_POS)
    }

    fun getCardNumber() : String {
        return isoMessage.getString(CARD_NUMBER_POS)
    }

    fun getTransactionAmmount() : BigDecimal {
        return BigDecimal(isoMessage.getString(TRANSACTION_AMMOUNT_POS))
    }

    fun asAuthorizationRequest() : AuthorizationRequest {
        return br.com.authk.rpc.authorization.AuthorizationRequest.newBuilder()
            .setCardBin(getCardBin())
            .setCardNumber(getCardNumber())
            .setTransactionAmmount(getTransactionAmmount().toDouble())
            .build()

    }


}