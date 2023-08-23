package com.hawolt.impl.capsolver;

import org.json.JSONObject;

/**
 * Created: 23/08/2023 9:13
 * Author: Twitter @hawolt
 **/

public enum CapSolverError {
    NO_ERROR(0, "OK"),
    ERROR_UNKNOWN(-1, "Failed to find a matching Error"),
    ERROR_KEY_DENIED_ACCESS(1, "Please check if your clientKey key is correct, get it in the personal center");

    private final String description;
    private final int id;

    CapSolverError(int id, String description) {
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    private static final CapSolverError[] ERROR_TYPES = CapSolverError.values();

    private static CapSolverError findById(int id) {
        for (CapSolverError errorType : ERROR_TYPES) {
            if (errorType.getId() == id) return errorType;
        }
        return CapSolverError.ERROR_UNKNOWN;
    }

    public static CapSolverError find(JSONObject response) {
        if (!response.has("errorId")) return CapSolverError.NO_ERROR;
        return findById(response.getInt("errorId"));
    }

}
