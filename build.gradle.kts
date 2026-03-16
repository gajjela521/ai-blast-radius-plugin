plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.blastradius"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Basic dependencies for JSON/HTTP if needed
    implementation("org.json:json:20231013")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.8.0")
}

intellij {
    version.set("2023.2.5")
    type.set("IC") // IntelliJ IDEA Community Edition

    plugins.set(listOf("com.intellij.java", "Git4Idea", "org.jetbrains.plugins.github"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<Test> {
        useJUnitPlatform()
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("252.*")
    }
}
