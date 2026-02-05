# Add project specific ProGuard rules here.

# Supabase
-keep class io.github.jan.supabase.** { *; }
-keep class io.ktor.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.kinder.petnames.**$$serializer { *; }
-keepclassmembers class com.kinder.petnames.** {
    *** Companion;
}
-keepclasseswithmembers class com.kinder.petnames.** {
    kotlinx.serialization.KSerializer serializer(...);
}
