package de.shadowdara.videoconverter

import de.shadowdara.daras_library.printProgressBar
import java.io.File

// Beispiel-Signatur deiner convert-Funktion
fun convert(profile: String, exportFolder: String, files: List<String>, config: Config) {
    println("Converting with profile $profile to $exportFolder")

    val total = files.size
    var current = 0

    files.forEach { filePath ->
        current++
        printProgressBar(current, total)
        println(" $current/$total: Converting File: $filePath")

        val inputFile = File(filePath)
        val outputFile = File(exportFolder, inputFile.name).absolutePath

        val args = config.get_args()
        val finalArgs = args.find { it.name == profile }?.arguments

        val (exitCode, output) = if (isWindows()) {
            runCommand("cmd", "/c", config.get_ffmpeg(), "-y", "-i",
                filePath, *(finalArgs ?: emptyList()).toTypedArray(), outputFile)
        } else {
            runCommand(config.get_ffmpeg(), "-y", "-i", filePath,
                *(finalArgs ?: emptyList()).toTypedArray(), outputFile)
        }

        println("Exit code: $exitCode")
        println("Output:\n$output")
    }

    println("\nFinished Conversion for all Files!")
}

// function to play a video with ffplay
fun play_video(config: Config, file: String) {
    val ffplay = config.get_ffplay()

    println("Playing Video: $file")

    val (exitCode, output) = if (isWindows()) {
        runCommand("cmd", "/c", ffplay, file)

    } else {
        runCommand(ffplay, file)
    }

    println("Exit code: $exitCode")
    println("Output:\n$output")
}
