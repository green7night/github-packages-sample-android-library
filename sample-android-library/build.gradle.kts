import java.io.FileInputStream
import java.util.Properties

plugins {
    id ("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

val githubProperties = Properties()
githubProperties.load(FileInputStream(rootProject.file("github.properties")))

fun getVersionName(): String {
    return "0.0.1"
}

fun getArtificatId(): String {
    return "sample-android-library"
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                run {
                    groupId = "com.green7night"
                    artifactId = getArtificatId()
                    version = getVersionName()
                    artifact("$buildDir/outputs/aar/${getArtificatId()}-release.aar")
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/green7night/github-packages-sample-android-library")
                credentials {
                    username = githubProperties.get("github_username") as String? ?: System.getenv("github_username")
                    password = githubProperties.get("github_access_token") as String? ?: System.getenv("github_access_token")
                }
            }
            maven {
                name = "CustomMavenRepo"
                url = uri("file://${buildDir}/repo")
            }
        }
    }
}

dependencies {
}