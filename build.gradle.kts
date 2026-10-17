import cat.freya.lodestone.License
import cat.freya.lodestone.excludeKotlin

plugins {
    alias(libs.plugins.lodestone)
}

group = "cat.freya.velocitywhitelist"

version = "0.2-SNAPSHOT"

description = "A Whitelist for Velocity"

lodestone {
    jvm(25)
    buildInfo {
        put("id", "velocitywhitelist")
        put("version", version)
        put("name", "Velocity Whitelist")
        put("author", "KenshinEto")
        put("description", description.toString())
        put("url", "https://g.freya.cat/VelocityWhitelist")
    }
    resources("velocity-plugin.json")
    generateBuildInfo()
    detekt()
    spotless()
    shadow {
        includeKotlin = true
        relocations.add("com.fasterxml.jackson")
        relocations.add("com.zaxxer.hikari")
        relocations.add("org.jetbrains.exposed")
        relocations.add("org.yaml.snakeyaml")
    }
    publish {
        group = "cat.freya"
        artifact = "VelocityWhitelist"
        author = "KenshinEto"
        url = "https://g.freya.cat/VelocityWhitelist"
        license = License.GPL3
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.freya.cat/snapshots")
}

dependencies {
    compileOnly(libs.velocity.api)
    shadow(libs.alloy)
    shadow(libs.exposed.core, excludeKotlin)
    shadow(libs.exposed.jdbc, excludeKotlin)
}
