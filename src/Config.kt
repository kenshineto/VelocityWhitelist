package cat.freya.velocitywhitelist

import cat.freya.alloy.annotation.*
import cat.freya.alloy.db.Driver.DatabaseConfig

data class Config(
    @Section("General") var enabled: Boolean = false,
    var debug: Boolean = false,
    var kickMessage: String = "You are not whitelisted on this server!",
    @Section("Database") var database: DatabaseConfig = DatabaseConfig(),
)
