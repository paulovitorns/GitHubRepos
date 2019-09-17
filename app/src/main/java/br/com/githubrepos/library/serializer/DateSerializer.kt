package br.com.githubrepos.library.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {

    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("WithCustomDefault")

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(df.format(obj))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())
    }
}
