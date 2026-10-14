package cat.freya.velocitywhitelist.command

import cat.freya.velocitywhitelist.Command
import cat.freya.velocitywhitelist.VelocityWhitelist
import cat.freya.velocitywhitelist.WhitelistEntry
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import kotlin.collections.List

class Add : Command() {
    override val label = "add"
    override val usage = listOf("player")

    override fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>) {
        val (name) = args

        val entry = WhitelistEntry.fetchFromMojang(plugin, name)
        if (entry == null) {
            source.sendPlainMessage("$name is not a valid user")
            return
        }

        plugin.database?.add(entry) ?: return
        source.sendPlainMessage("$name added to whitelist")
    }

    override fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String) =
        when (param) {
            "player" -> plugin.server.allPlayers.map(Player::getUsername).filter { it.startsWith(typed) }
            else -> listOf()
        }
}
