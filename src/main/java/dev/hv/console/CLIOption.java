package dev.hv.console;

import java.util.ArrayList;

public abstract class CLIOption {
    String name;
    String shorthand_name;
    String description;
    ArrayList<CLIOption> requiredArgs;

    CLIOption(String name, String shorthand_name, String description, ArrayList<CLIOption> requiredArgs) {
        this.name = name;
        this.shorthand_name = shorthand_name;
        this.description = description;
        this.requiredArgs = requiredArgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShorthand_name() {
        return shorthand_name;
    }

    public void setShorthand_name(String shorthand_name) {
        this.shorthand_name = shorthand_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<CLIOption> getRequiredArgs() {
        return requiredArgs;
    }

    public void setRequiredArgs(ArrayList<CLIOption> requiredArgs) {
        this.requiredArgs = requiredArgs;
    }
}
