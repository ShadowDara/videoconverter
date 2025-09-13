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
                // 🎥 Standardqualität, gut für die meisten Zwecke
                Profile("Normal",
                    listOf("-c:v", "libx264", "-preset", "slow", "-crf", "23", "-c:a", "aac", "-b:a", "192k")),

                // 🎮 Speziell für Replay Mod Aufnahmen
                Profile("Replay Mod Recording",
            listOf("-c:v", "libx264", "-preset", "fast", "-crf", "18", "-pix_fmt", "yuv420p", "-r", "60", "-c:a", "aac", "-b:a", "192k")),

                // 📈 Sehr hohe Qualität, langsame Verarbeitung
                Profile("High Quality (x264)",
            listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "18", "-c:a", "aac", "-b:a", "256k")),

                // ⚡ Schnell & effizient
                Profile("Fast Encoding (x264)",
            listOf("-c:v", "libx264", "-preset", "ultrafast", "-crf", "28", "-c:a", "aac", "-b:a", "128k")),

                // 🖥️ Für Hardwarebeschleunigung (Nvidia GPU)
                Profile("NVIDIA GPU (h264_nvenc)",
            listOf("-c:v", "h264_nvenc", "-preset", "p4", "-cq", "23", "-b:v", "5M", "-c:a", "aac", "-b:a", "192k")),

                // 📱 Für Mobile Devices / YouTube
                Profile("YouTube / Mobile Optimized",
            listOf("-c:v", "libx264", "-preset", "slow", "-crf", "23", "-pix_fmt", "yuv420p", "-movflags", "+faststart", "-c:a", "aac", "-b:a", "160k")),

                // 💾 Sehr kleine Dateigröße
                Profile("Small File (Low Quality)",
            listOf("-c:v", "libx264", "-preset", "veryfast", "-crf", "32", "-c:a", "aac", "-b:a", "96k")),

                // 🧼 Lossless (ohne Qualitätsverlust)
                Profile("Lossless (x264)",
            listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "0", "-c:a", "flac")),

                // 🌈 HEVC/H.265 – bessere Kompression, braucht stärkere Geräte
                Profile("HEVC (x265)",
            listOf("-c:v", "libx265", "-preset", "slow", "-crf", "28", "-c:a", "aac", "-b:a", "160k")),

                // 🔥 NVIDIA GPU mit HEVC
                Profile("NVIDIA GPU (HEVC)",
            listOf("-c:v", "hevc_nvenc", "-preset", "p5", "-cq", "28", "-c:a", "aac", "-b:a", "160k")),

                // 🎮 Twitch Streaming Recording
                Profile("Twitch Recording",
            listOf("-c:v", "libx264", "-preset", "faster", "-crf", "22", "-r", "30", "-c:a", "aac", "-b:a", "128k")),

                // 📸 Nur Video extrahieren (ohne Audio)
                Profile("No Audio",
            listOf("-an", "-c:v", "libx264", "-preset", "medium", "-crf", "23")),

                // 🎧 Nur Audio extrahieren
                Profile("Audio Only (MP3)",
            listOf("-vn", "-c:a", "libmp3lame", "-b:a", "192k")),

                // 🧪 Testprofile (Null-Ausgabe)
                Profile("Benchmark (No Output)",
            listOf("-f", "null", "-")),

                // 📼 Interlaced Video Encoding
                Profile("DVD Interlaced (MPEG-2)",
            listOf("-target", "pal-dvd", "-c:v", "mpeg2video", "-c:a", "ac3", "-b:a", "192k")),

                // 🐢 Super Slow Quality Upload
                Profile("Very High Quality (Slow Upload)",
            listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "16", "-c:a", "aac", "-b:a", "320k")),

            )
        )
    }
}
