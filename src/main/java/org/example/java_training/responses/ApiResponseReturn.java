package org.example.java_training.responses;

public class ApiResponseReturn<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public ApiResponseReturn(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
