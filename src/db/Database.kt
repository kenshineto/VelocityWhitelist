package cat.freya.velocitywhitelist.db

import cat.freya.alloy.db.Driver
import cat.freya.velocitywhitelist.VelocityWhitelist
import cat.freya.velocitywhitelist.WhitelistEntry
import java.util.UUID
import kotlin.io.path.pathString
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.Database as Exposed
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class Database(plugin: VelocityWhitelist) {
    private val driver =
        Driver.getDriver(plugin.config.database, plugin.dataDirectory.resolve("whitelist.db").pathString)
    private val source = driver.connect()
    private val db = Exposed.connect(source)

    init {
        transaction(db) { SchemaUtils.create(Whitelist) }
    }

    fun getEntry(uuid: UUID): WhitelistEntry? =
        transaction(db) {
            val id = uuid.toString()
            Whitelist.selectAll().where { Whitelist.uuid eq id }.map(ResultRow::toWhitelistEntry).singleOrNull()
        }

    fun isInWhtielist(uuid: UUID): Boolean = getEntry(uuid) != null

    fun add(entry: WhitelistEntry) {
        transaction(db) {
            val id = entry.uuid.toString()
            val current =
                Whitelist.selectAll().where { Whitelist.uuid eq id }.map(ResultRow::toWhitelistEntry).singleOrNull()
            if (current == null) {
                Whitelist.insert {
                    it[uuid] = entry.uuid.toString()
                    it[name] = entry.name
                }
            } else {
                Whitelist.update({ Whitelist.uuid eq id }) { it[name] = entry.name }
            }
        }
    }

    fun remove(name: String): Boolean =
        transaction(db) {
            Whitelist.deleteWhere { Whitelist.name eq name } > 0
        }

    fun list(): List<WhitelistEntry> =
        transaction(db) {
            Whitelist.selectAll().map(ResultRow::toWhitelistEntry).toList()
        }

    fun clear() {
        transaction(db) {
            Whitelist.deleteAll()
        }
    }
}
