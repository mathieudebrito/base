package com.github.mathieudebrito.base.utils;

public class Objects {

    public static String name(Object object) {
        if (object == null) {
            return "UnknownClass";
        }

        String className = "UnknownClass";
        if (object instanceof Class) {
            className = ((Class) object).getCanonicalName();
        } else {
            className = object.getClass().toString();
        }
        String[] path = className.split("\\.");
        className = path[path.length - 1];
        if (className.endsWith("_")) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }
}
