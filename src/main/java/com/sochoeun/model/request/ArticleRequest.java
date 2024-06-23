package com.sochoeun.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {

    private String name;
    private int categoryId;
}
