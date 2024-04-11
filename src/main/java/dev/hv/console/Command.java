package dev.hv.console;

import java.util.ArrayList;

public interface Command {

    boolean process(ArrayList<String> args);

    boolean checkArguments();
}
