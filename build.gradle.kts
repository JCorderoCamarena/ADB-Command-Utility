import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
}

group = "me.jorgecordero"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    // Run Shell Scripts
    implementation("com.lordcodes.turtle:turtle:0.5.0")
    implementation("br.com.devsrsouza.compose.icons.jetbrains:font-awesome:1.0.0")
    implementation("br.com.devsrsouza.compose.icons.jetbrains:feather:1.0.0")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ADB-Command-Utility"
            packageVersion = "1.0.0"
        }
    }
}