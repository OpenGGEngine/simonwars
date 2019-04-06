package com.opengg.wars;

import java.util.ArrayList;
import java.util.List;

public class Command {
    String command;
    List<String> args = new ArrayList<>();

    public Command(String command, List<String> args) {
        this.command = command;
        this.args = args;
    }

    public static Command create(String command, List<String> args){
        return new Command(command, args);
    }

    public static Command create(String command, String... args){
        return new Command(command, List.of(args));
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", args=" + args +
                '}';
    }
}
