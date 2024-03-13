plugins {
    `kotlin-dsl`
}

group = "com.m.twitchwatchdog.buildlogic"

dependencies {
    compileOnly("com.android.tools.build:gradle:8.3.0")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
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