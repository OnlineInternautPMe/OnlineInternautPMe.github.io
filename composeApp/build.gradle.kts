import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        wasmJsMain.dependencies {
            implementation(npm("three", "0.171.0"))
        }
    }
}

tasks.register<Copy>("buildDeploymentAndMoveToDocs") {
    group = "distribution"
    description = "Copies the web distribution to the root docs folder"

    // 1. Define the source: the output of the production distribution task
    val distributionTask = tasks.named("wasmJsBrowserDistribution")
    from(distributionTask)

    // 2. Define the destination: 'docs' folder at the project root
    // rootProject.projectDir refers to the very top level of your KMP project
    into(rootProject.projectDir.resolve("docs"))

    // 3. Optional: Clean the docs folder before copying to avoid stale files
    doFirst {
        delete(rootProject.projectDir.resolve("docs"))
    }
}