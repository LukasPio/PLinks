package com.lucas.plinks.utils;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private String timestamp;
    private String elapsedTime;

    public ApiResponse() {
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
    }

    public ApiResponse(String status, String message, T data, String elapsedTime) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
        this.elapsedTime = elapsedTime;
    }

}