plugins {
    java
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("Day01")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
