package cat.freya.velocitywhitelist

import cat.freya.alloy.fetchJson
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.UUID

private class MojangUUIDDeserializer : JsonDeserializer<UUID>() {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): UUID {
        val id = p.text
        val formatted = buildString {
            append(id.substring(0, 8))
            append('-')
            append(id.substring(8, 12))
            append('-')
            append(id.substring(12, 16))
            append('-')
            append(id.substring(16, 20))
            append('-')
            append(id.substring(20))
        }
        return UUID.fromString(formatted)
    }
}

data class WhitelistEntry(
    @JsonAlias("id") @JsonDeserialize(using = MojangUUIDDeserializer::class) val uuid: UUID,
    var name: String,
) {
    companion object {
        fun fetchFromMojang(plugin: VelocityWhitelist, name: String): WhitelistEntry? {
            val endpoint = "https://api.mojang.com/users/profiles/minecraft/${name}"
            val userAgent = "${BuildInfo.name} ${BuildInfo.version}"
            return fetchJson<WhitelistEntry>(endpoint, userAgent)
                .onFailure {
                    plugin.logger.error(it.message)
                }
                .getOrDefault(null)
        }
    }
}
