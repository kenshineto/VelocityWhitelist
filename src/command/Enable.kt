package cat.freya.velocitywhitelist.command

import cat.freya.velocitywhitelist.Command
import cat.freya.velocitywhitelist.VelocityWhitelist
import com.velocitypowered.api.command.CommandSource
import kotlin.collections.List

class Enable : Command() {
    override val label = "enable"
    override val usage = emptyList<String>()

    override fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>) {
        if (plugin.config.enabled) {
            source.sendPlainMessage("Whitelist is already enabled")
            return
        }

        plugin.config.enabled = true
        plugin.saveConfig()
        source.sendPlainMessage("Whitelist has been enabled")
    }

    override fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String): List<String> {
        return listOf()
    }
}
