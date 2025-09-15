plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "1.9.10"
}

group = "de.shadowdara.videoconverter"
version = "0.1.5-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation(files("libs/daras_library-0.1.5-SNAPSHOT.jar"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "de.shadowdara.videoconverter.MainKt"
        }

        // nur dein Code rein, keine dependencies
        from(sourceSets.main.get().output)

        // optional: verhindern, dass ungewollt was reingepackt wird
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
}
