package dev.hv.console;

import java.util.ArrayList;

public interface Command {

    void process(ArrayList<String> args);

    boolean loadArguments();
}
