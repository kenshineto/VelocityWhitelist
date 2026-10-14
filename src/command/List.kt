package cat.freya.velocitywhitelist.command

import cat.freya.velocitywhitelist.Command
import cat.freya.velocitywhitelist.VelocityWhitelist
import cat.freya.velocitywhitelist.WhitelistEntry
import com.velocitypowered.api.command.CommandSource
import kotlin.collections.List

class List : Command() {
    override val label = "list"
    override val usage = emptyList<String>()

    override fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>) {
        val entires = plugin.database?.list() ?: return
        if (entires.isEmpty()) {
            source.sendPlainMessage("Whitelist is empty")
            return
        }

        val msg = entires.joinToString(" ", transform = WhitelistEntry::name)
        source.sendPlainMessage(msg)
    }

    override fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String): List<String> {
        return listOf()
    }
}
