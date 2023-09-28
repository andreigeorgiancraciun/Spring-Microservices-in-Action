package com.optimagrowth.license.service.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static UserContext createEmptyContext() {
        return new UserContext();
    }

    public static void clear() {
        userContext.remove();
    }
}
