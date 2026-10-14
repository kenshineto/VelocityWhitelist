package cat.freya.velocitywhitelist.command

import cat.freya.velocitywhitelist.Command
import cat.freya.velocitywhitelist.VelocityWhitelist
import com.velocitypowered.api.command.CommandSource
import kotlin.collections.List

class Disable : Command() {
    override val label = "disable"
    override val usage = emptyList<String>()

    override fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>) {
        if (!plugin.config.enabled) {
            source.sendPlainMessage("Whitelist is already disabled")
            return
        }

        plugin.config.enabled = false
        plugin.saveConfig()
        source.sendPlainMessage("Whitelist has been disabled")
    }

    override fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String): List<String> {
        return listOf()
    }
}
