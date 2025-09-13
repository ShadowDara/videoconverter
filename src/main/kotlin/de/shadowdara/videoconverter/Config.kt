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
                    listOf("-c:v", "libx264", "-preset", "slow", "-crf", "23",
                        "-c:a", "aac", "-b:a", "192k")),

                // 🎮 Speziell für Replay Mod Aufnahmen
                Profile("Replay Mod Recording",
            listOf("-c:v", "libx264", "-preset", "fast", "-crf", "18", "-pix_fmt",
                "yuv420p", "-r", "60", "-c:a", "aac", "-b:a", "192k")),

                // 📈 Sehr hohe Qualität, langsame Verarbeitung
                Profile("High Quality (x264)",
            listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "18", "-c:a",
                "aac", "-b:a", "256k")),

                // ⚡ Schnell & effizient
                Profile("Fast Encoding (x264)",
            listOf("-c:v", "libx264", "-preset", "ultrafast", "-crf", "28", "-c:a",
                "aac", "-b:a", "128k")),

                // 🖥️ Für Hardwarebeschleunigung (Nvidia GPU)
                Profile("NVIDIA GPU (h264_nvenc)",
            listOf("-c:v", "h264_nvenc", "-preset", "p4", "-cq", "23", "-b:v",
                "5M", "-c:a", "aac", "-b:a", "192k")),

                // 📱 Für Mobile Devices / YouTube
                Profile("YouTube / Mobile Optimized",
            listOf("-c:v", "libx264", "-preset", "slow", "-crf", "23", "-pix_fmt",
                "yuv420p", "-movflags", "+faststart", "-c:a", "aac", "-b:a", "160k")),

                // 💾 Sehr kleine Dateigröße
                Profile("Small File (Low Quality)",
            listOf("-c:v", "libx264", "-preset", "veryfast", "-crf", "32", "-c:a",
                "aac", "-b:a", "96k")),

                // 🧼 Lossless (ohne Qualitätsverlust)
                Profile("Lossless (x264)",
            listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "0", "-c:a",
                "flac")),

                // 🌈 HEVC/H.265 – bessere Kompression, braucht stärkere Geräte
                Profile("HEVC (x265)",
            listOf("-c:v", "libx265", "-preset", "slow", "-crf", "28", "-c:a",
                "aac", "-b:a", "160k")),

                // 🔥 NVIDIA GPU mit HEVC
                Profile("NVIDIA GPU (HEVC)",
            listOf("-c:v", "hevc_nvenc", "-preset", "p5", "-cq", "28", "-c:a",
                "aac", "-b:a", "160k")),

                // 🎮 Twitch Streaming Recording
                Profile("Twitch Recording",
            listOf("-c:v", "libx264", "-preset", "faster", "-crf", "22", "-r",
                "30", "-c:a", "aac", "-b:a", "128k")),

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
            listOf("-target", "pal-dvd", "-c:v", "mpeg2video", "-c:a", "ac3",
                "-b:a", "192k")),

                // 🐢 Super Slow Quality Upload
                Profile("Very High Quality (Slow Upload)",
                    listOf("-c:v", "libx264", "-preset", "veryslow", "-crf", "16",
                "-c:a", "aac", "-b:a", "320k")),

                // 🆕 AV1 – Standardqualität, gute Kompression
                Profile("AV1 (Standard Quality)",
                listOf("-c:v", "libaom-av1", "-crf", "30", "-b:v", "0",
                    "-preset", "medium", "-c:a", "libopus", "-b:a", "128k")),

                // 🆕 AV1 – Hohe Qualität (langsamer)
                Profile("AV1 (High Quality)",
                listOf("-c:v", "libaom-av1", "-crf", "23", "-b:v", "0",
                    "-preset", "slow", "-c:a", "libopus", "-b:a", "192k")),

                // 🆕 AV1 – NVIDIA Hardware (RTX 30xx+)
                Profile("AV1 (NVIDIA NVENC)",
                listOf("-c:v", "av1_nvenc", "-cq", "28", "-preset", "p5",
                    "-c:a", "aac", "-b:a", "160k")),

                // AV1 für YouTube / Web (optimiert für Streaming-Plattformen)
                Profile("AV1 for YouTube/Web",
                    listOf("-c:v", "libaom-av1", "-crf", "30", "-b:v", "0",
                        "-preset", "good", "-pix_fmt", "yuv420p", "-movflags", "+faststart",
                        "-c:a", "libopus", "-b:a", "160k")),

                Profile("Archival (Lossless FFV1)",
                    listOf("-c:v", "ffv1", "-level", "3", "-c:a", "flac")),

                Profile("Social Media (Mobile)",
                    listOf("-c:v", "libx264", "-preset", "fast", "-crf", "24",
                        "-vf", "scale=720:1280", "-r", "30", "-pix_fmt", "yuv420p", "-c:a",
                        "aac", "-b:a", "128k")),

                Profile("Proxy (Superfast Preview)",
                    listOf("-c:v", "libx264", "-preset", "ultrafast", "-crf",
                        "35", "-c:a", "aac", "-b:a", "96k")),

                // Container wechseln
                Profile("Remux (No Re-encoding)",
                    listOf("-c", "copy")),

                Profile("Audio Only (Opus)",
                    listOf("-vn", "-c:a", "libopus", "-b:a", "128k")),

                // AV1 is still better
                Profile("VP9 (WebM)",
                    listOf("-c:v", "libvpx-vp9", "-crf", "32", "-b:v", "0",
                        "-c:a", "libopus", "-b:a", "128k")),

                Profile("Split by 10 min",
                    listOf("-f", "segment", "-segment_time", "600",
                        "-reset_timestamps", "1", "-c:v", "libx264", "-crf", "23",
                        "-preset", "medium", "-c:a", "aac", "-b:a", "128k")),

                Profile("Screen Capture (Lossless)",
                    listOf("-c:v", "libx264rgb", "-preset", "ultrafast",
                        "-crf", "0", "-pix_fmt", "rgb24", "-c:a", "flac")),

                Profile("GIF (Palette Optimized)",
                    listOf("-vf", "fps=10,scale=480:-1:flags=lanczos,palettegen",
                        "-y", "palette.png")),
                Profile("GIF (Final)",
                    listOf("-i", "palette.png", "-lavfi",
                        "fps=10,scale=480:-1:flags=lanczos [x]; [x][1:v] paletteuse",
                        "-loop", "0")),

                Profile("MPEG-2 (Set-Top Box)",
                    listOf("-c:v", "mpeg2video", "-q:v", "5", "-c:a", "mp2",
                        "-b:a", "192k", "-target", "pal-dvd")),

                Profile("Visually Lossless (x264)",
                    listOf("-c:v", "libx264", "-preset", "slow", "-crf", "16",
                        "-c:a", "aac", "-b:a", "256k")),

                Profile("Gaming (NVIDIA AV1, High FPS)",
                    listOf("-c:v", "av1_nvenc", "-preset", "p4", "-cq", "23",
                        "-rc", "vbr", "-b:v", "8M", "-r", "120", "-c:a", "aac", "-b:a",
                        "160k")),

                Profile("Podcast / YouTube Audio Normalized",
                    listOf("-af", "loudnorm=I=-16:TP=-1.5:LRA=11", "-c:v",
                        "copy", "-c:a", "aac", "-b:a", "192k")),

                Profile("Two-Pass (1080p @ 4Mbit)",
                    listOf("-c:v", "libx264", "-b:v", "4M", "-pass", "1",
                        "-an", "-f", "mp4", "/dev/null")),
                Profile("Two-Pass (Final)",
                    listOf("-c:v", "libx264", "-b:v", "4M", "-pass", "2",
                        "-c:a", "aac", "-b:a", "192k")),

                Profile("Square Format (1:1)",
                    listOf("-vf", "scale=720:720", "-c:v", "libx264",
                        "-preset", "medium","-crf", "23", "-pix_fmt", "yuv420p", "-c:a",
                        "aac", "-b:a", "128k")),
            )
        )
    }
}
