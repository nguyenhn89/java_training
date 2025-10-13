package org.example.java_training.responses;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public ApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public static <T> void success(T data, String message)
    {
        System.out.println("Message: " + message);
        System.out.println("data: "+ data);
    }

    public static <T> ApiResponseReturn<T> success(String message)
    {
        return new ApiResponseReturn<>(true, 200, message, null);
    }
}
