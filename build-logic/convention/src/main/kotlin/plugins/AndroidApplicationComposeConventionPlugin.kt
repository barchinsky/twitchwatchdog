package plugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureAndroidCompose
import extensions.configureKotlinAndroid
import extensions.findVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
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
                defaultConfig.targetSdk = findVersion("targetSdk").toInt()
                defaultConfig.minSdk = findVersion("minSdk").toInt()
                compileSdk = findVersion("compileSdk").toInt()
            }
        }
    }
}