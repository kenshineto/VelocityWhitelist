package cat.freya.velocitywhitelist

import com.velocitypowered.api.command.CommandSource

abstract class Command {
    abstract val label: String
    abstract val usage: List<String>

    abstract fun execute(plugin: VelocityWhitelist, source: CommandSource, args: List<String>)

    abstract fun autoComplete(plugin: VelocityWhitelist, param: String, typed: String): List<String>
}
