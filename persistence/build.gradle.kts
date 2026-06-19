/**
 * Subproject: persistence
 * Artifact  : dev.jhipster.persistence
 *
 * Lightweight plugin — no LangChain4j, Docker, pgvector or MCP dependencies.
 * The convention plugin `jhipster.gradle-plugin-conventions` configures
 * Kotlin, Java, testing and publishing.
 */
plugins {
    `java-library`
    signing
    `maven-publish`
    `java-gradle-plugin`
//    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.publish)
    id("jhipster.gradle-plugin-conventions")
}

group = "education.cccp"

version = libs.versions.persistence.get()

dependencies {
    implementation(libs.kotlin.stdlib)

    testImplementation(libs.junit.jupiter)
    testImplementation(gradleTestKit())
    testRuntimeOnly(libs.junit.platform.launcher)
}

gradlePlugin {
    website = "https://github.com/cheroliv/jhipster-gradle-plugins"
    vcsUrl  = "https://github.com/cheroliv/jhipster-gradle-plugins"
    plugins {
        create("jhipsterPersistence") {
            id = "education.cccp.jhipster.persistence"
            implementationClass = "dev.jhipster.persistence.JHipsterPersistencePlugin"
            displayName         = "JHipster Persistence Plugin"
            description         = """
                Orchestrates the JHipster regeneration cycle (clean/generate/sync)
                without losing the Kotlin business code persisted in __codebase__/.
                Convention-based resolution — no DSL extension required.
            """.trimIndent()
            tags = listOf("jhipster", "kotlin", "codegen", "persistence")
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set(gradlePlugin.plugins.getByName("jhipsterPersistence").displayName)
                description.set(gradlePlugin.plugins.getByName("jhipsterPersistence").description)
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

java {
    withJavadocJar()
    withSourcesJar()
}
