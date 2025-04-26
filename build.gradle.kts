plugins {
    id("java")
    application
}

group = "org.RedBlackTree"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.test {
    useJUnitPlatform()
}
val mainClass = "org.example.Main"

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.RedBlackTree.Main"
    }
    from(sourceSets.main.get().output)
}


application {
    mainClass.set("org.RedBlackTree.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
