package cat.freya.velocitywhitelist.command

import cat.freya.velocitywhitelist.Command
import cat.freya.velocitywhitelist.VelocityWhitelist
import cat.freya.velocitywhitelist.WhitelistEntry
import com.velocitypowered.api.command.CommandSource
import kotlin.collections.List

class Remove : Command() {
    override val label = "remove"
    override val usage = listOf("player")

    override fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>) {
        val (name) = args

        val res = plugin.database?.remove(name)
        if (res != true) {
            source.sendPlainMessage("$name is not in the whitelist")
        } else {
            source.sendPlainMessage("$name has been removed from the whitelist")
        }
    }

    override fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String) =
        when (param) {
            "player" -> (plugin.database?.list() ?: listOf()).map(WhitelistEntry::name).filter { it.startsWith(typed) }
            else -> listOf()
        }
}
