package com.hawolt.cli;

import java.util.*;

/**
 * <p>CLI class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class CLI extends HashSet<Argument> {

    private final Map<Argument, List<String>> INSTRUCTIONS = new HashMap<>();

    /**
     * <p>put.</p>
     *
     * @param arg a {@link com.hawolt.cli.Argument} object
     * @throws com.hawolt.cli.ParserException if any.
     */
    public void put(Argument arg) throws ParserException {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (arg.isUnique() && (argument.getShortName().equals(arg.getShortName()) || argument.getLongName().equals(arg.getLongName()))) {
                throw new ParserException(String.format("Unique argument [%s|%s] already present", arg.getShortName(), arg.getLongName()));
            }
        }
        if (!INSTRUCTIONS.containsKey(arg)) INSTRUCTIONS.put(arg, new ArrayList<>());
        INSTRUCTIONS.get(arg).add(arg.getValue());
    }

    /**
     * <p>has.</p>
     *
     * @param key a {@link java.lang.String} object
     * @return a boolean
     */
    public boolean has(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.containsKey(argument);
            }
        }
        return false;
    }

    /**
     * <p>get.</p>
     *
     * @param key a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    public List<String> get(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.get(argument);
            }
        }
        return null;
    }

    /**
     * <p>getValue.</p>
     *
     * @param key a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String getValue(String key) {
        for (Argument argument : INSTRUCTIONS.keySet()) {
            if (argument.getLongName().equals(key) || argument.getShortName().equals(key)) {
                return INSTRUCTIONS.get(argument).get(0);
            }
        }
        return null;
    }

    /**
     * <p>isArgument.</p>
     *
     * @param s a {@link java.lang.String} object
     * @return a boolean
     */
    protected boolean isArgument(String s) {
        return s.startsWith("--") || s.startsWith("-");
    }

    /**
     * <p>getHelp.</p>
     *
     * @return a {@link java.lang.String} object
     */
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

    /**
     * <p>convert.</p>
     *
     * @param in a {@link java.lang.String} object
     * @return an array of {@link java.lang.String} objects
     */
    public static String[] convert(String in) {
        String[] actual = new String[0];
        String[] basic = in.split(" ");
        int offset = 0;
        for (int i = 0; i < basic.length; i++) {
            String current = basic[i];
            if (!current.startsWith("\"")) {
                actual = Arrays.copyOf(actual, actual.length + 1);
                actual[offset++] = basic[i];
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(current.substring(1));
                do {
                    int index = ++i;
                    if (i >= basic.length) break;
                    current = basic[index];
                    if (!current.endsWith("\"")) {
                        builder.append(" ").append(current);
                    } else {
                        builder.append(" ").append(current, 0, current.length() - 1);
                    }
                } while (!current.endsWith("\""));
                actual = Arrays.copyOf(actual, actual.length + 1);
                actual[offset++] = builder.toString();
            }
        }
        return actual;
    }
}
