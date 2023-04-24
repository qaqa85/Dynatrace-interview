package com.dynatrace.currency.utils;

import org.springframework.http.HttpStatusCode;

import java.util.function.Predicate;

public class StatusCode {
    public static Predicate<HttpStatusCode> isStatusCode404() {
        return isStatusCode(404);
    }

    public static Predicate<HttpStatusCode> isStatusCode400() {
        return isStatusCode(400);
    }

    private static Predicate<HttpStatusCode> isStatusCode(int code) {
        return httpStatusCode -> httpStatusCode.isSameCodeAs(HttpStatusCode.valueOf(code));
    }
}
