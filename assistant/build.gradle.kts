/**
 * Sous-projet : assistant
 * Artefact    : dev.jhipster.assistant
 *
 * Plugin d'assistance au code — dépendances lourdes isolées ici.
 * Statut : SNAPSHOT — non publié.
 */
plugins {
    signing
    id("jhipster.gradle-plugin-conventions")
    alias(libs.plugins.kotlin.serialization)
}

group = "education.cccp"

version = libs.versions.assistant.get()

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.serialization.json)

    // RAG
    implementation(libs.langchain4j.core)
    implementation(libs.langchain4j.pgvector)
    implementation(libs.langchain4j.embeddings.minilm)
    implementation(libs.langchain4j.ollama)
    implementation(libs.langchain4j.gemini)
    implementation(libs.langchain4j.mistral)

    // Infrastructure pgvector
    implementation(libs.docker.java.core)
    implementation(libs.docker.java.httpclient5)
    implementation(libs.postgresql.jdbc)

    testImplementation(libs.junit.jupiter)
    testImplementation(gradleTestKit())
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit5)
}

gradlePlugin {
    website = "https://github.com/cheroliv/jhipster-gradle-plugins"
    vcsUrl  = "https://github.com/cheroliv/jhipster-gradle-plugins"

    plugins {
        create("jhipsterAssistant") {
            id                  = "education.cccp.jhipster.assistant"
            implementationClass = "dev.jhipster.assistant.JHipsterAssistantPlugin"
            displayName         = "JHipster Assistant Plugin"
            description         = """
                Assistance au code Kotlin JHipster via RAG pgvector et LLM multi-provider.
                Conversation AsciiDoc persistée.
                Documentation officielle indexée (JHipster, Kotlin, Gradle, Arrow).
            """.trimIndent()
            tags = listOf(
                "jhipster", "kotlin", "rag", "ai",
                "langchain4j", "pgvector", "assistant"
            )
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set(gradlePlugin.plugins.getByName("jhipsterAssistant").displayName)
                description.set(gradlePlugin.plugins.getByName("jhipsterAssistant").description)
                url.set(gradlePlugin.website.get())
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("cccp-education")
                        name.set("CCCP Education")
                        email.set("cccp.edu@gmail.com")
                    }
                }
                scm {
                    connection.set(gradlePlugin.vcsUrl.get())
                    developerConnection.set(gradlePlugin.vcsUrl.get())
                    url.set(gradlePlugin.vcsUrl.get())
                }
                project.findProperty("relocationGroup")?.let { targetGroup ->
                    withXml {
                        val pom = asElement()
                        val doc = pom.ownerDocument
                        val distMgmt = doc.createElement("distributionManagement")
                        val relocation = doc.createElement("relocation")
                        relocation.appendChild(doc.createElement("groupId")).also { it.textContent = targetGroup.toString() }
                        relocation.appendChild(doc.createElement("artifactId")).also { it.textContent = project.name }
                        distMgmt.appendChild(relocation)
                        pom.appendChild(distMgmt)
                    }
                }
            }
        }
    }
    repositories {
        mavenCentral()
    }
}

signing {
    if (System.getenv("CI") != "true" && !version.toString().endsWith("-SNAPSHOT")) {
        sign(publishing.publications)
    }
    useGpgCmd()
}
