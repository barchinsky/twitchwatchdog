package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.8"
        }

        dependencies {
            add("implementation", libs.findLibrary("activity-compose").get())
            add("implementation", platform(libs.findLibrary("compose-bom").get()))
            add("implementation", "androidx.compose.ui:ui-tooling-preview")
            add("implementation", "androidx.compose.ui:ui-graphics")
            add("implementation", "androidx.compose.material3:material3")
            add("debugImplementation", "androidx.compose.ui:ui-tooling")
            add("debugImplementation", "androidx.compose.ui:ui-test-manifest")
            add("androidTestImplementation", platform(libs.findLibrary("compose-bom").get()))
        }
    }
}