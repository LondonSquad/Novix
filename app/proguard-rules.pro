# Keep all your modules
-keep class com.london.** { *; }
-keep interface com.london.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Keep Koin generated
-keep class org.koin.ksp.generated.** { *; }

# Ignore missing classes that are safe to ignore
-dontwarn java.lang.management.**
-dontwarn org.slf4j.**