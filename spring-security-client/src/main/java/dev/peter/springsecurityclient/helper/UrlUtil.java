package dev.peter.springsecurityclient.helper;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String generateApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
