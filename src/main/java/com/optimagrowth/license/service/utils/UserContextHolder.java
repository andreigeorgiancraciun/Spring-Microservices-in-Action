package com.optimagrowth.license.service.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHolder {
    private static final ThreadLocal<UserContext> USER_CONTEXT = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = USER_CONTEXT.get();

        if (context == null) {
            context = createEmptyContext();
            USER_CONTEXT.set(context);

        }
        return USER_CONTEXT.get();
    }

    public static UserContext createEmptyContext() {
        return new UserContext();
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }
}
