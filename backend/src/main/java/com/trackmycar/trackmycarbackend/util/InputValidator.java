package com.trackmycar.trackmycarbackend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static boolean isValidHexColor(String markerHexColor) {

        if (markerHexColor == null || markerHexColor.isEmpty()) {
            return false;
        }

        // Define the regular expression for a valid hexadecimal color code
        String hexColorPattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

        Pattern pattern = Pattern.compile(hexColorPattern);

        Matcher matcher = pattern.matcher(markerHexColor);

        return matcher.matches();
    }
}
