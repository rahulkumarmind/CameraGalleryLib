plugins {
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

android {
    namespace = "com.rahul.cameragalleryhelper"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
}


//// This block ensures that the publishing configurations are evaluated after the project has been evaluated
//afterEvaluate {
//    android.libraryVariants.forEach { variant ->
//
//        publishing.publications.create(variant.name, MavenPublication::class) {
//
//            from(components.findByName(variant.name))
//
//            groupId = "com.github.rahulkumarmind"
//            artifactId = "CameraGalleryLib"
//            version = "1.0.0"
//        }
//    }
//}



    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {

                    from(components.findByName("release"))

                    groupId = "com.github.rahulkumarmind"
                    artifactId = "CameraGalleryLib"
                    version = "1.0.0"
                }
            }
        }
    }
