package de.shadowdara.videoconverter

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class Config(private val version: String, val filePath: String) {
    var config: ConfigData

    fun load_config() {

            val jsonString = File(this.filePath).readText()

            val json = Json {
                ignoreUnknownKeys = true  // <== ignoriert unerwartete JSON-Keys
                prettyPrint = true
                isLenient = true          // erlaubt flexiblere Formate
            }

            config = json.decodeFromString<ConfigData>(jsonString)
    }

    fun save_config() {
        try {
            val json = Json { prettyPrint = true } // Optional: schöneres Format
            val jsonString = json.encodeToString(this.config)
            File(this.filePath).writeText(jsonString)
            println("Config saved in: $filePath")
        } catch (e: Exception) {
            println("Error while saving the Config:")
            e.printStackTrace()
        }
    }

    fun get_version(): String {
        return this.config.version
    }

    fun set_version() {
        this.config.version = version
    }

    fun get_args(): List<Profile> {
        return this.config.profiles
    }

    fun get_ffmpeg(): String {
        return this.config.ffmpeg
    }

    init {
        config = ConfigData(
            version = this.version,
            edittime = "",
            ffmpeg = "ffmpeg",
            profiles = listOf(
                Profile("Normal",
                    listOf("-c:v", "libx264", "-preset",
                    "slow", "-crf", "23", "-c:a", "copy")),
                Profile("Replay Mod Recordings",
                    listOf("-c:v", "libx264", "-preset", "fast",
                        "-crf", "18", "-pix_fmt", "yuv420p", "-r", "60",
                        "-c:a", "aac", "-b:a", "192k")),
                Profile("Better Quality",
                    listOf("-c:v", "libx264", "-preset",
                        "veryslow", "-crf", "18", "-c:a", "aac", "-b:a", "192k")),
                Profile("Normal + Nvidia GPU",
                    listOf("-c:v", "h264_nvenc", "-preset", "slow",
                        "-cq", "23", "-c:a", "copy")),
            )
        )
    }
}
