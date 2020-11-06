
# yinElderService Android App

![Screenshot_1604634177](https://user-images.githubusercontent.com/3708465/98338498-1ff07900-1fbf-11eb-82af-eccefc7e58dc.png =37.5 x 66.6)
Screenshots

About the components
The app uses Android JAVA to build its objects and views. Java and XML files is first generated by
android gallery template and modified accordingly.

Backend uses bitnami Parse Server which provides full developed features for apps. Parse project
started by facebook and continue by  https://parseplatform.org/

Dependency

Parse modules needs to be added to build.gradle and build.gradle(module) file.
Navigation component also requires additional dependency files.

build.gradle:
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

build.gradle(module):
dependencies {
    ...
    implementation "androidx.navigation:navigation-compose:1.0.0-alpha01"
    implementation 'com.github.parse-community.Parse-SDK-Android:parse:1.25.0'
}




