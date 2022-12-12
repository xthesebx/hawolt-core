package com.hawolt;

/**
 * Created: 12/12/2022 10:47
 * Author: Twitter @hawolt
 **/

public enum FileType {
    CERTIFICATE, PRIVATE_KEY;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
