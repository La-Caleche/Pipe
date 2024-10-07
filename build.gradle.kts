plugins {
    id("fr.lacaleche.caldle") version "1.2.3"
}

dependencies {
    api(libs.caleche.core)

    compileOnlyApi(libs.adventure.text.minimessage)

    testImplementation(libs.caleche.core)
    testImplementation(libs.commons.lang3)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
