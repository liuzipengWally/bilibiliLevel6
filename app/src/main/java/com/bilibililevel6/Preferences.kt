package com.bilibililevel6

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * author：liuzipeng
 * time: 2022/12/20 21:55
 */
@Serializable
data class Preferences(
    val qrCodeKey: String = "",
    val refreshToken: String = "",
    val isLogged: Boolean = false,
    val loggedCookie: String = ""
)

object PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue = Preferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        try {
            return Json.decodeFromString(
                Preferences.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(Preferences.serializer(), t).encodeToByteArray())
        }
    }
}