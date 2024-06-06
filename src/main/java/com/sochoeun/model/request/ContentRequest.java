package com.sochoeun.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest {
    private String title;
    private String description;
    private int articleId;
}
