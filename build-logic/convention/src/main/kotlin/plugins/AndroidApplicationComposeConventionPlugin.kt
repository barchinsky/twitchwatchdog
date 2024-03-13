package plugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureAndroidCompose
import extensions.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.com.jcraft.jsch.ConfigRepository.defaultConfig
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.android")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            configureKotlinAndroid(extension)

            extension.apply {
                defaultConfig.targetSdk = 34
                defaultConfig.minSdk = 28
                compileSdk = 34
            }
        }
    }
}