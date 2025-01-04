plugins {
    java
    application
    id("org.danilopianini.gradle-java-qa") version "1.77.0"
}

group = "it.unibo.exam" // Nome del gruppo del progetto
version = "1.0-SNAPSHOT" // Versione del progetto

repositories {
    mavenCentral() // Repository Maven Central
}

dependencies {
    // Aggiungi dipendenze se necessario
    //implementation("org.swinglabs.swingx:swingx-all:1.6.1") // Esempio di libreria Swing avanzata
}

application {
    mainClass.set("it.unibo.exam.bin.Main") // Nome completo della classe principale
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8" // Imposta l'encoding
}
