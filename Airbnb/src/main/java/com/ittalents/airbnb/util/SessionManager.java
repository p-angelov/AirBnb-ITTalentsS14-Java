package com.ittalents.airbnb.util;

import com.ittalents.airbnb.model.exceptions.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;

public class SessionManager {
    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";
    public static void validateLogin(HttpServletRequest request) {
        boolean newSession = request.getSession().isNew();
        boolean logged = request.getSession().getAttribute(LOGGED) != null && ((Boolean) request.getSession().getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(request.getSession().getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("You are not logged in");
        }
    }
}
