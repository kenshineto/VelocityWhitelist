package cat.freya.velocitywhitelist

import cat.freya.velocitywhitelist.command.Add
import cat.freya.velocitywhitelist.command.Disable
import cat.freya.velocitywhitelist.command.Enable
import cat.freya.velocitywhitelist.command.Remove
import com.velocitypowered.api.command.SimpleCommand

class CommandManager(private val plugin: VelocityWhitelist) : SimpleCommand {
    private val registry: MutableMap<String, Command> = mutableMapOf()

    init {
        register(Add(), Remove(), Enable(), Disable())
        register(cat.freya.velocitywhitelist.command.List())
    }

    private fun register(vararg commands: Command) {
        for (command in commands) {
            registry[command.label] = command
        }
    }

    private fun getCommand(invocation: SimpleCommand.Invocation): Pair<Command?, List<String>> {
        val args = invocation.arguments()
        val source = invocation.source()

        val label = args.firstOrNull()
        val command = label?.let { registry[it] }

        return command to args.drop(1)
    }

    override fun execute(invocation: SimpleCommand.Invocation) {
        val (command, args) = getCommand(invocation)
        val source = invocation.source()
        if (command == null) {
            source.sendPlainMessage("Invalid command.")
            return
        }
        if (args.size < command.usage.size) {
            source.sendPlainMessage("Not enough arguments.")
            return
        }
        command.execute(plugin, source, args)
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        val (command, _) = getCommand(invocation)
        if (command == null) return false
        val source = invocation.source()
        return source.hasPermission("vwl.${command.label}")
    }

    override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val (command, args) = getCommand(invocation)
        val typed = args.lastOrNull() ?: ""
        if (command == null) {
            return registry.keys.filter { it.startsWith(typed) }
        }
        val index = (args.size - 1).coerceAtLeast(0)
        val param = command.usage.getOrNull(index) ?: return listOf()
        return command.autoComplete(plugin, param, typed)
    }
}
