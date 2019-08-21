package br.com.authk.data

import br.com.authk.rpc.authorization.AuthorizationRequest
import org.jpos.iso.ISOMsg
import java.math.BigDecimal

class InternalAuthorizationRequest(isoMsg : ISOMsg) {
    private val isoMessage = isoMsg

    private val binPosition = 3
    private val cardNumberPosition = 4
    private val transactionAmountPosition = 7

    fun getCardBin() : String {
        return isoMessage.getString(binPosition)
    }

    fun getCardNumber() : String {
        return isoMessage.getString(cardNumberPosition)
    }

    fun getTransactionAmount() : BigDecimal {
        return BigDecimal(isoMessage.getString(transactionAmountPosition))
    }

    fun asAuthorizationRequest() : AuthorizationRequest {
        return br.com.authk.rpc.authorization.AuthorizationRequest.newBuilder()
            .setCardBin(getCardBin())
            .setCardNumber(getCardNumber())
            .setTransactionAmmount(getTransactionAmount().toDouble())
            .build()

    }


}