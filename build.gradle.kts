dependencies {
    api(project(":library:CalecheCore"))

    compileOnlyApi(libs.adventure.text.minimessage)

    testImplementation(project(":library:CalecheCore"))
    testImplementation(libs.commons.lang3)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
