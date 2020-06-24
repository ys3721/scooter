plugins {
    java
    application
}

dependencies {
    implementation("io.netty:netty-all:4.1.49.Final")
    implementation("org.apache.logging.log4j:log4j-api:2.13.3")
    implementation("org.apache.logging.log4j:log4j-core:2.13.3")
    implementation("commons-cli:commons-cli:1.4");
    implementation("com.google.protobuf:protobuf-java:3.12.2")
}

application {
    mainClassName = "com.iceicelee.scooter.tools.BasicTool" 
}