plugins {
    kotlin("jvm") version "2.0.0-Beta1"
}

kotlin.jvmToolchain(21)

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
