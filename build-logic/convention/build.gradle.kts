plugins {
    `kotlin-dsl`
}

group = "com.m.twitchwatchdog.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "twitchwatchdog.android.application.compose"
            implementationClass = "plugins.AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "twitchwatchdog.android.library.compose"
            implementationClass = "plugins.AndroidLibraryComposeConventionPlugin"
        }
    }
}