plugins {
    id("java")
    id ("com.gradleup.shadow") version "8.3.0"
}

group = "dev.mikan"
version = "1.8.8"

val outputDir = file("/home/mikan/Desktop/localhosts/1_8/plugins")

tasks.register<Copy>("copy"){
    dependsOn(tasks.named("jar"))
    from(tasks.named("jar").get().outputs.files)
    into(outputDir)
}

tasks.register<Exec>("updateRepo"){
    workingDir = file("/home/mikan/IdeaProjects/AltairKit-1.8.8/build/libs/")
    commandLine(
        "mvn",
        "install:install-file",
        "-Dfile=Altair-1.8.8.jar",
        "-DgroupId=dev.mikan",
        "-DartifactId=AltairKit",
        "-Dversion=1.8.8",
        "-Dpackaging=jar"
    )}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>(){
    archiveClassifier.set("")
    relocate("dev.mikan.altairkit", "dev.mikan.shaded.altairkit")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }

    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("org.slf4j:slf4j-simple:2.0.13")
}

tasks.named("build"){
//    dependsOn(tasks.named("shadowJar"))
    finalizedBy("copy")
    finalizedBy("updateRepo")

}
