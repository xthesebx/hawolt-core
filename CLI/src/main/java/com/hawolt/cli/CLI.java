package com.hawolt.cli;

import java.util.*;

public class CLI extends HashSet<Argument> {

    private final Map<Argument, List<String>> INSTRUCTIONS = new HashMap<>();

    public void put(Argument arg) throws ParserException {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (arg.isUnique() && (argument.getShortName().equals(arg.getShortName()) || argument.getLongName().equals(arg.getLongName()))) {
                throw new ParserException(String.format("Unique argument [%s|%s] already present", arg.getShortName(), arg.getLongName()));
            }
        }
        if (!INSTRUCTIONS.containsKey(arg)) INSTRUCTIONS.put(arg, new ArrayList<>());
        INSTRUCTIONS.get(arg).add(arg.getValue());
    }

    public boolean has(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.containsKey(argument);
            }
        }
        return false;
    }

    public List<String> get(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.get(argument);
            }
        }
        return null;
    }

    public String getValue(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.get(argument).get(0);
            }
        }
        return null;
    }

    protected boolean isArgument(String s) {
        return s.startsWith("--") || s.startsWith("-");
    }

    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-16s | %-9s | %-9s | %-9s | %-5s | %s\n", "Command", "Shortcut", "Required", "Argument", "Once", "Description"));
        for (Argument argument : this) {
            builder.append(String.format("%-16s | %-9s | %-9s | %-9s | %-5s | %s\n",
                    argument.getLongName(),
                    argument.getShortName(),
                    argument.isMandatory(),
                    argument.isFlag(),
                    argument.isUnique(),
                    argument.getDescription())
            );
        }
        return builder.toString();
    }
}
