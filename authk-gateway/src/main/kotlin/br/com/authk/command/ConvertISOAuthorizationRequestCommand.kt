package br.com.authk.command

import br.com.authk.context.Context
import br.com.authk.data.InternalAuthorizationRequest
import br.com.authk.data.ContextKey
import br.com.authk.data.Message
import org.jpos.iso.IFE_NUMERIC
import org.jpos.iso.ISOMsg
import org.jpos.iso.packager.GenericPackager
import java.nio.charset.StandardCharsets

/**
 * Convert ISO 8583 into internal format
 */
class ConvertISOAuthorizationRequestCommand : Command {
    private val isoPackager = GenericPackager("c:/tmp/isomap_bin.xml")
    private val expectedMessageType = "0110"

    override fun isApplicableFor(ctx: Context): Boolean {
        val msg = ctx.get(Context.MESSAGE_ID_KEY.toString()) as Message
        return msg != null && expectedMessageType == msg.messageType
    }

    override fun execute(ctx: Context) : Boolean {
        val msg = ctx.get(Context.MESSAGE_ID_KEY.toString()) as Message

        if(msg.rawMessage != null) {
            val isoMsg = ISOMsg()
            isoMsg.packager = isoPackager


            isoMsg.unpack(msg.rawMessage!!.toByteArray(StandardCharsets.UTF_8))

            ctx.save(ContextKey.OPERATION_REQUEST_ID.toString(), InternalAuthorizationRequest(isoMsg))
        }else{
            //TODO: return parsing rejection
            return true;
        }

        return false;
    }
}