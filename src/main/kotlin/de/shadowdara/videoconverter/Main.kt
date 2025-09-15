package de.shadowdara.videoconverter

import de.shadowdara.daras_library.io.JarExtractor.extractFile
import de.shadowdara.daras_library.io.getCallerJarDirectory
import java.io.File
import javax.swing.*

fun main() {
    val version = "0.1.4"

    val jarDir = getCallerJarDirectory()
    val settingsFile = File(jarDir, "config.videoconverter.json")
    val config = Config(version, settingsFile.absolutePath)

    println("VideoConverter v$version")

    try {
        config.load_config()

        if (config.get_version() == "") {
            config.set_version()
            config.save_config()
        }
    } catch (e: Exception) {
        println("Could not load Config File!")
        println(e)
        config.save_config()
    }

    // Profile laden
    val profiles = config.get_args()
    val profileNames: Array<String> = profiles.map { it.name }.toTypedArray()

    // === UI-Erstellung ===
    SwingUtilities.invokeLater {
        createUI(profileNames, config)
    }
}

fun isWindows() = System.getProperty("os.name").lowercase().contains("win")

fun runCommand(vararg command: String): Pair<Int, String> {
    return try {
        val processBuilder = ProcessBuilder(*command)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()
        exitCode to output
    } catch (e: Exception) {
        e.printStackTrace()
        -1 to e.message.orEmpty()
    }
}

fun extractInfo() {
    extractFile("profile-info/de.html", "profile-info/de.html")
    extractFile("profile-info/en.html", "profile-info/en.html")
    extractFile("profile-info/style.css", "profile-info/style.css")
}
