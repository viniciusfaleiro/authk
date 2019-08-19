package br.com.authk.data

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

data class Message(
    var messageType: String? = null,
    var producerHost: String? = null,
    var rawMessage: String? = null,
    var payload: Any? = null,
    var sequence: Long ? = null,
    var uniqueId: UUID = UUID.randomUUID(),
    var correlationId: UUID? = null,
    var podId: String ? = null,
    var timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")))
