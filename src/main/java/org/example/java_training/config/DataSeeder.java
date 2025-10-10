package org.example.java_training.config;

import net.datafaker.Faker;
import org.example.java_training.domain.ProductDetail;
import org.example.java_training.repository.ProductRepository;
import org.example.java_training.repository.CategoryRepository;
import org.example.java_training.repository.TagRepository;
import org.example.java_training.repository.ProductDetailRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.example.java_training.domain.Product;
import org.example.java_training.domain.Category;
import org.example.java_training.domain.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepo,
            TagRepository tagRepo,
            ProductRepository productRepo,
            ProductDetailRepository productDetailRepo
    ) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();

            // 🟢 Seed Categories
            if (categoryRepo.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Category category = new Category();
                    category.setName(faker.commerce().department());
                    categoryRepo.save(category);
                }
                System.out.println("✅ Seeded 10 categories");
            }

            // 🟢 Seed Tags
            if (tagRepo.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Tag tag = new Tag();
                    tag.setName(faker.commerce().material());
                    tagRepo.save(tag);
                }
                System.out.println("✅ Seeded 10 tags");
            }

            // 🟢 Seed Products + ProductDetails
            if (productRepo.count() == 0) {
                List<Category> categories = categoryRepo.findAll();

                for (int i = 1; i <= 100; i++) {
                    // ProductDetail
                    ProductDetail detail = new ProductDetail();
                    detail.setDescription(faker.lorem().paragraph());
                    detail.setSpecification(faker.commerce().promotionCode());
                    productDetailRepo.save(detail);

                    // Product
                    Product product = new Product();
                    product.setName(faker.commerce().productName());
                    product.setPrice(BigDecimal.valueOf(Double.parseDouble(faker.commerce().price())));
                    product.setCategoryId((long) Math.toIntExact(categories.get(random.nextInt(categories.size())).getId()));
                    product.setContent(faker.lorem().sentence());
                    product.setMemo(faker.lorem().word());
                    productRepo.save(product);
                }
                System.out.println("✅ Seeded 100 products (with details)");
            }
        };
    }
}
