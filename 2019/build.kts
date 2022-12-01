plugins {
    kotlin("jvm") version "1.4.20"
}

repositories {
    mavenCentral()
}

sourceSets {
    val main by getting
    main.java.srcDirs("src/main/kotlin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
