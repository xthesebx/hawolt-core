package com.hawolt.cli;

public class Parser extends CLI {

    public CLI check(String[] args) throws ParserException {
        CLI result = new CLI();
        for (Argument argument : this) {
            boolean found = false;
            for (int i = 0; i < args.length; i++) {
                String current = args[i];
                if (isArgument(current)) {
                    int index = current.lastIndexOf("-") + 1;
                    String raw = current.substring(index);
                    if ((argument.getShortName().equals(raw) && index == 1) || (argument.getLongName().equals(raw) && index == 2)) {
                        found = true;
                        if (!argument.isFlag()) {
                            int next = ++i;
                            if (next < args.length) {
                                argument.setValue(args[next]);
                            } else {
                                throw new ParserException(String.format("Missing argument for %s", raw));
                            }
                        }
                        result.put(argument);
                    }
                }
            }
            if (!found && argument.isMandatory()) {
                throw new ParserException(String.format("Could not find required argument %s", argument.getLongName()));
            }
        }
        return result;
    }
}
