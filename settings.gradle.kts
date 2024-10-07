pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        maven {
            name = "La Calèche Private"
            url = uri("https://reposilite.lacaleche.cc/private")
            credentials {
                username = System.getenv("REPOSILITE_TOKEN_NAME")
                    ?: providers.gradleProperty("lc.reposilite.readonly.name").orNull
                            ?: throw GradleException("No REPOSILITE_TOKEN_NAME environment variable or lc.reposilite.readonly.name property found")
                password = System.getenv("REPOSILITE_TOKEN_SECRET")
                    ?: providers.gradleProperty("lc.reposilite.readonly.token").orNull
                            ?: throw GradleException("No REPOSILITE_TOKEN_SECRET environment variable or lc.reposilite.readonly.token property found")
            }
        }
    }
}

if (!providers.environmentVariable("CI").isPresent) {
    includeBuild("../CalecheCore") {
        dependencySubstitution {
            substitute(module("fr.lacaleche.core:calechecore")).using(project(":"))
        }
    }
}
