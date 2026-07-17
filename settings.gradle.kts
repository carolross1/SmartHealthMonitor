pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Eclipse Paho MQTT (Sesión 13 - MQTT HiveMQ Cloud)
        maven { url = uri("https://repo.eclipse.org/content/repositories/paho-releases/") }
    }
}

rootProject.name = "SmartHealthMonitor"
include(":app")
include(":wear")
include(":tv")
