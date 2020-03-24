package br.com.authk.data

import br.com.authk.rpc.authorization.AuthorizationRequest
import org.jpos.iso.ISOMsg
import java.math.BigDecimal

class InternalAuthorizationRequest(requestId : String, isoMsg : ISOMsg) {
    private val requestId = requestId
    private val isoMessage = isoMsg
    private val accountNumberPos = 2                /* Field 2 */
    private val processingCodePos = 3               /* Field 3 */
    private val transactionAmountPos = 4            /* Field 4 */
    private val dateTimePos = 7                     /* Field 7 */
    private val systemTraceAuditNumberPos = 11      /* Field 11 */
    private val acquiringIdPos = 32                 /* Field 32 */
    private val merchantIdPos = 42                  /* Field 42 */

    fun getAccountNumber() : String {
        return isoMessage.getString(accountNumberPos)
    }

    fun getProcessingCode() : String {
        return isoMessage.getString(processingCodePos)
    }

    fun getTransactionAmount() : Double {
        val decimalPrecision = 2

        var rawValue = isoMessage.getString(transactionAmountPos)
        var integerValue = rawValue.substring(0, rawValue.length - decimalPrecision)
        var decimalValue = rawValue.substring(rawValue.length - decimalPrecision , rawValue.length)

        return BigDecimal(integerValue).plus((BigDecimal(decimalValue).div(BigDecimal(100)))).toDouble()
    }

    fun getDateTime() : String {
        return isoMessage.getString(dateTimePos)
    }

    fun getSystemTraceAuditNumber() : String {
        return isoMessage.getString(systemTraceAuditNumberPos)
    }

    fun getAcquiringId() : String{
        return isoMessage.getString(acquiringIdPos)
    }

    fun getMerchantId() : String {
        return isoMessage.getString(merchantIdPos)
    }

    fun getRequestId() : String{
        return requestId
    }

    fun asAuthorizationRequest() : AuthorizationRequest {
        return AuthorizationRequest.newBuilder()
            .setRequestId(getRequestId())
            .setAccountNumber(getAccountNumber())
            .setProcessingCode(getProcessingCode())
            .setTransactionAmount(getTransactionAmount())
            .setDateTime(getDateTime())
            .setSystemTraceAuditNumber(getSystemTraceAuditNumber())
            .setAcquiringId(getAcquiringId())
            .setMerchantId(getMerchantId()).build()

    }


}