import cat.freya.lodestone.excludeKotlin
import cat.freya.lodestone.publishing.License

plugins {
    alias(libs.plugins.lodestone)
}

group = "cat.freya.velocitywhitelist"

version = "0.2-SNAPSHOT"

description = "A Whitelist for Velocity"

lodestone {
    java(25)
    kotlin()
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
        artifact = "VelocityWhitelist"
        group = "cat.freya"
        author = "KenshinEto"
        license = License.GPL3
        gitRepo = "https://g.freya.cat/VelocityWhitelist"
        reposilite("https://maven.freya.cat")
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
