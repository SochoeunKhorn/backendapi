package com.sochoeun.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String message,
        int statusCode,
        @JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")
        LocalDateTime localDateTime
) {
}
