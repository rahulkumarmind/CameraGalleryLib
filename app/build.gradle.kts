plugins {
    alias(libs.plugins.androidApplication)
    id("maven-publish")
}

android {
    namespace = "com.rahul.CameraGalleryLib"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rahul.CameraGalleryLib"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}


val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // Group ID, artifact ID and version for the Maven artifact
                groupId = "com.github.rahulkumarmind"
                artifactId = "CameraGalleryLib"
                version = "1.0.0"

                // Use the release build variant component for publishing
                from(components.findByName("release"))

                // Assuming the sourcesJar task is properly configured
                artifact(sourcesJar.get())
            }
        }
        repositories {
            mavenLocal()
        }
    }
}
