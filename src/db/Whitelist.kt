package cat.freya.velocitywhitelist.db

import cat.freya.velocitywhitelist.WhitelistEntry
import java.util.UUID
import org.jetbrains.exposed.v1.core.*

object Whitelist : Table("velocity_whitelist") {
    val uuid = varchar("uuid", 36)
    val name = text("name")

    override val primaryKey = PrimaryKey(uuid)
}

fun ResultRow.toWhitelistEntry(): WhitelistEntry {
    return WhitelistEntry(
        uuid = UUID.fromString(this[Whitelist.uuid]),
        name = this[Whitelist.name],
    )
}
