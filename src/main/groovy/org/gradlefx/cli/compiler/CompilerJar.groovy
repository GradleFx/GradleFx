package org.gradlefx.cli.compiler

/**
 * Represents one of the
 */
enum CompilerJar {

    compc("compc.jar"), //Flex SDK
    compc_cli("compc-cli.jar"), //provided in the newest AIR sdks
    mxmlc("mxmlc.jar"), //Flex SDK
    mxmlc_cli("mxmlc-cli.jar"), //provided in the newest AIR sdks
    asdoc("asdoc.jar"), //Flex SDK
    asdoc_legacy("legacy/asdoc.jar") //provided in the newest AIR sdks

    private String jarName

    private CompilerJar(String jarName) {
        this.jarName = jarName
    }

    String getJarName() {
        return jarName
    }

    @Override
    public String toString() {
        return jarName
    }
}
