package de.shadowdara.videoconverter

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    var version: String,
    var edittime: String,
    var ffmpeg: String,
    var ffplay: String,
    var profiles: List<Profile>
)

@Serializable
data class Profile(
    var name: String,
    var arguments: List<String>
)
