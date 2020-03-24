package br.com.authk.interpreter

import br.com.authk.interpreter.prefixer.HexPrefixer
import org.jpos.iso.*

class IFH_LLNUM : ISOStringFieldPackager(NullPadder.INSTANCE, AsciiInterpreter.INSTANCE, HexPrefixer.LL) {
}