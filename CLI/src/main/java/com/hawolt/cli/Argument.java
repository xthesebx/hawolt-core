package com.hawolt.cli;

/**
 * <p>Argument class.</p>
 *
 * @author Hawolt
 * @version 1.1
 */
public class Argument {

    private final String shortName, longName, description;
    private final boolean mandatory, unique, flag;
    private String value;

    /**
     * <p>create.</p>
     *
     * @param shortName a {@link java.lang.String} object
     * @param longName a {@link java.lang.String} object
     * @param description a {@link java.lang.String} object
     * @param mandatory a boolean
     * @param unique a boolean
     * @param flag a boolean
     * @return a {@link com.hawolt.cli.Argument} object
     */
    public static Argument create(String shortName, String longName, String description, boolean mandatory, boolean unique, boolean flag) {
        return new Argument(shortName, longName, description, mandatory, unique, flag);
    }

    private Argument(String shortName, String longName, String description, boolean mandatory, boolean unique, boolean flag) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.mandatory = mandatory;
        this.unique = unique;
        this.flag = flag;
    }

    /**
     * <p>Getter for the field <code>shortName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * <p>Getter for the field <code>longName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getLongName() {
        return longName;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>isMandatory.</p>
     *
     * @return a boolean
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * <p>isUnique.</p>
     *
     * @return a boolean
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * <p>isFlag.</p>
     *
     * @return a boolean
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param value a {@link java.lang.String} object
     */
    public void setValue(String value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int strings = shortName.hashCode() + longName.hashCode() + description.hashCode();
        int booleans = (mandatory ? 31 : 1) * (unique ? 37 : 1) * (flag ? 41 : 1);
        return 7 * (strings + booleans);
    }
}
