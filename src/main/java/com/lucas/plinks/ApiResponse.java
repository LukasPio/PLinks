package com.lucas.plinks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    String message;
    Timestamp timestamp;
    int statusCode;
    T data;
}
