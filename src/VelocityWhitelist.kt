package cat.freya.velocitywhitelist

import cat.freya.alloy.config.deserialize
import cat.freya.alloy.config.serialize
import cat.freya.velocitywhitelist.db.Database
import com.google.inject.Inject
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.io.File
import java.nio.file.Path
import net.kyori.adventure.text.Component
import org.slf4j.Logger

const val CONFIG_FILE_NAME = "config.yml"

@Plugin(id = BuildInfo.id, name = BuildInfo.name, version = BuildInfo.version, url = BuildInfo.url)
class VelocityWhitelist {
    val server: ProxyServer
    val logger: Logger
    val dataDirectory: Path

    var config: Config = Config()
        private set

    var database: Database? = null
        private set

    @Inject
    constructor(server: ProxyServer, logger: Logger, @DataDirectory dataDirectory: Path) {
        this.server = server
        this.logger = logger
        this.dataDirectory = dataDirectory

        val commandManager = server.commandManager
        val meta = commandManager.metaBuilder("vwl").build()
        commandManager.register(meta, CommandManager(this))

        logger.info("Starting ${BuildInfo.name}")
        reloadConfig()
            .onFailure {
                logger.warn("Plugin loaded with errors :(")
            }
            .onSuccess {
                logger.info("Plugin loaded successfully!")
            }
    }

    fun readConfig(): Config {
        val dir = dataDirectory.toFile()
        if (!dir.exists()) dir.mkdirs() || error("Failed to make plugin config directory")
        val file = File(dir, CONFIG_FILE_NAME)
        val ins = if (file.exists()) file.inputStream() else null
        return deserialize(Config::class, ins)
    }

    fun saveConfig() {
        val dir = dataDirectory.toFile()
        if (!dir.exists()) dir.mkdirs() || error("Failed to make plugin config directory")
        val file = File(dir, CONFIG_FILE_NAME)
        file.writeText(serialize(config))
    }

    fun reloadConfig(): Result<Unit> {
        return runCatching {
            logger.info("Loading configuration")
            config = readConfig()
            logger.info("Loading database")
            database = Database(this)
        }
            .onSuccess {
                saveConfig()
            }
            .onFailure {
                logger.error("Failed to reload config: ${it.message}")
                if (config.debug) {
                    it.stackTrace.forEach { line -> logger.error(line.toString()) }
                }
            }
    }

    @Subscribe
    fun onPlayerLogin(event: LoginEvent) {
        if (!config.enabled) return

        val player = event.player
        if (player == null) {
            logger.error("LoginEvent triggered with a null player.")
            return
        }

        if (config.debug) {
            logger.debug("Player login: " + player.username)
        }

        val whitelisted = database?.isInWhtielist(player.uniqueId)
        if (whitelisted != true) {
            if (config.debug) {
                logger.debug("Player ${player.username} kicked: $whitelisted")
            }
            event.result = ResultedEvent.ComponentResult.denied(Component.text(config.kickMessage))
        }
    }
}
