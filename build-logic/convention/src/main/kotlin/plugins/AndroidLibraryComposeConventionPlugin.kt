package plugins

import com.android.build.api.dsl.LibraryExtension
import extensions.configureAndroidCompose
import extensions.configureKotlinAndroid
import extensions.findVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
            configureKotlinAndroid(extension)

            extension.defaultConfig.minSdk = findVersion("minSdk").toInt()
        }
    }
}