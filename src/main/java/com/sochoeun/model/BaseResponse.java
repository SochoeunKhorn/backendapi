package com.sochoeun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus httpStatus;
    private String message;
    private Object object;

    public void success(Object object){
        this.timestamp = LocalDateTime.now();
        this.httpStatus =  HttpStatus.OK;
        this.message = "Success";
        this.object = object;
    }

}
