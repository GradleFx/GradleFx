package org.gradlefx.cli.compiler

/**
 * Allows to compose compiler options in an easy manner.
 */
class CompilerOptions {

    private final List<String> arguments = []

    void each(Closure closure) {
        arguments.each(closure)
    }

    boolean contains(GString option) {
        arguments.contains(option)
    }

    List<String> asList() {
        arguments.clone()
    }

    void set(CompilerOption option, String value) {
        arguments.add "$option.optionName=$value"
    }

    void set(CompilerOption option, Collection<String> values) {
        arguments.add "$option.optionName=${values.join(',')}"
    }

    void reset(CompilerOption option) {
        arguments.add "$option.optionName="
    }

    void add(CompilerOption option, String value) {
        arguments.add "$option.optionName+=$value"
    }

    void add(CompilerOption option) {
        arguments.add option.optionName
    }

    void add(String value) {
        arguments.add value
    }

    void setAll(CompilerOption option, Collection<String> values) {
        values.each { set option, it }
    }

    void addAll(CompilerOption option, Collection<String> values) {
        values.each { add option, it }
    }

    void addAll(Collection<String> values) {
        arguments.addAll values
    }


    @Override
    public String toString() {
        return "CompilerOptions{" +
                "arguments=" + arguments +
                '}';
    }
}
