package br.com.authk.interpreter.prefixer

import org.jpos.iso.Prefixer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class HexPrefixer (nDigits: Int): Prefixer {

    companion object{
        val LL = HexPrefixer(2)
        val LLL = HexPrefixer(3)
    }

    private val nDigits = nDigits

    override fun encodeLength(length: Int, b: ByteArray) {
        var length = length
        length = length shl 1

        for (i in this.getPackedLength() - 1 downTo 0) {
            val twoDigits = length % 100
            length /= 100
            b[i] = ((twoDigits / 10 shl 4) + twoDigits % 10).toByte()
        }

    }

    override fun decodeLength(b: ByteArray, offset: Int): Int {
        var hexValue = String(b, StandardCharsets.UTF_8).subSequence(offset, offset + nDigits).toString()
        return Integer.parseInt(hexValue, 16)
    }

    override fun getPackedLength(): Int {
        return this.nDigits;
    }
}