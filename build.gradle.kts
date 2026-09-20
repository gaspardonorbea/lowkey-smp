plugins {
    java
}

group = "nl.lowkey"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${property("paperApi")}")
}

java {
    // Paper 26.x draait op een nieuwe Java. Krijg je een fout over de Java-versie? Pas dit getal aan.
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.jar {
    archiveFileName.set("LowkeyCore.jar")
}
