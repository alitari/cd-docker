package de.alexkrieg.persontracker.util;

import java.util.Optional;

import org.apache.commons.lang.Validate;

public class ValidationUtil {

    public static void validatePresent(Optional<?> entity, String message) {
        validatePresent0(entity, false, message);
    }

    public static void validateNotPresent(Optional<?> entity, String message) {
        validatePresent0(entity, true, message);
    }

    private static void validatePresent0(Optional<?> entity, boolean not, String message) {
        Validate.isTrue((!not && entity.isPresent()) || (not && !entity.isPresent()),
                message + (not ? " must " : " does ") + " not exist");
    }

}
