package com.sochoeun.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageUrl;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Transient
    private List<Media> mediaList;
}
