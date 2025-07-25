plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.icerock).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.room).apply(false)
}
