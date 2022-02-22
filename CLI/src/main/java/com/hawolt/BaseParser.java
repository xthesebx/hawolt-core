package com.hawolt;

import java.util.HashSet;

public class BaseParser extends HashSet<Argument> {
    public void put(Argument arg) throws ParserException {
        for (Argument argument : this) {
            if (arg.isUnique() && (argument.getShortName().equals(arg.getShortName()) || argument.getLongName().equals(arg.getLongName()))) {
                throw new ParserException(String.format("Unique argument [%s|%s] already present", arg.getShortName(), arg.getLongName()));
            }
        }
        System.setProperty(arg.getLongName(), arg.getValue());
        add(arg);
    }

    public boolean has(String key) {
        for (Argument argument : this) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) return true;
        }
        return false;
    }

    public Argument get(String key) {
        for (Argument argument : this) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) return argument;
        }
        return null;
    }

    public String getValue(String key) {
        for (Argument argument : this) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) return argument.getValue();
        }
        return null;
    }

    protected boolean isArgument(String s) {
        return s.startsWith("--") || s.startsWith("-");
    }

    public void print() {
        System.out.format("%-16s | %-9s | %-9s | %-9s | %-5s | %s\n", "Command", "Shortcut", "Required", "Argument", "Once", "Description");
        for (Argument argument : this) {
            System.out.format("%-16s | %-9s | %-9s | %-9s | %-5s | %s\n",
                    argument.getLongName(),
                    argument.getShortName(),
                    argument.isMandatory(),
                    argument.isFlag(),
                    argument.isUnique(),
                    argument.getDescription());
        }
    }
}
