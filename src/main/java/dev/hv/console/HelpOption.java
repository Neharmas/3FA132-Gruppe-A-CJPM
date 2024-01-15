package dev.hv.console;

import java.util.ArrayList;

public class HelpOption extends CLIOption {

    HelpOption(String name, String shorthand_name, String description, ArrayList<CLIOption> required_args) {
        super(name, shorthand_name, description, required_args);
    }
}
