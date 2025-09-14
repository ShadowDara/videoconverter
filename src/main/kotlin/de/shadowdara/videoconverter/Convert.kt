package de.shadowdara.videoconverter

import java.io.File

// Beispiel-Signatur deiner convert-Funktion
fun convert(profile: String, exportFolder: String, files: List<String>, config: Config) {
    println("Converting with profile $profile to $exportFolder")
    var x = 0

    files.forEach { _ ->
        x++
    }

    var y = 0

    files.forEach {
        y++
        println("$y/$x: Converting File: $it")

        val inputFile = File(it)
        val outputFile = File(exportFolder, inputFile.name).absolutePath

        val args = config.get_args()

        val finalArgs = args.find { it.name == profile }?.arguments


        val (exitCode, output) = if (isWindows()) {
            runCommand("cmd", "/c", config.get_ffmpeg(), "-y", "-i",
                it, *(finalArgs ?: emptyList()).toTypedArray(), outputFile)

        } else {
            runCommand(config.get_ffmpeg(), "-y", "-i", it,
                *(finalArgs ?: emptyList()).toTypedArray(), outputFile)
        }

        println("Exit code: $exitCode")
        println("Output:\n$output")
    }

    println("Finished Conversion for all Files!")
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
