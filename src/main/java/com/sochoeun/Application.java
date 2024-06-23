package com.sochoeun;

import com.sochoeun.model.Article;
import com.sochoeun.model.Category;
import com.sochoeun.repository.ArticleRepository;
import com.sochoeun.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {
	//private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			CategoryRepository categoryRepository,
			ArticleRepository articleRepository
	) {
		return args -> {

			if(categoryRepository.count() == 0) {
				categoryRepository.save(Category.builder()
						.nameKh("category")
						.nameEn("category")
						.build());
			}

			if(articleRepository.count() == 0) {
				Category category = categoryRepository.findById(1).orElseThrow();
				articleRepository.save(Article.builder().name("Article").category(category).build());
			}

		};
	}

}
