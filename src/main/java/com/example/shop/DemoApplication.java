package com.example.shop;

import com.example.shop.model.Category;
import com.example.shop.model.Product;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Value("${app.seed-size:20000}")
    private int seedSize;

    public DemoApplication(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepo.count() > 0) {
            System.out.println("✅ Datos ya existen — no se ejecuta seed.");
            return;
        }

        Faker faker = new Faker();
        Random rnd = new Random();

        System.out.println("🚀 Creando categorías...");
        List<Category> categories = new ArrayList<>();
        // Creamos algunas categorías iniciales
        categories.add(new Category(null, "Electrónica", "Productos electrónicos"));
        categories.add(new Category(null, "Ropa", "Prendas de vestir"));
        categories.add(new Category(null, "Hogar", "Artículos para el hogar"));
        categories.add(new Category(null, "Deporte", "Equipamiento deportivo"));
        categories.add(new Category(null, "Libros", "Libros y material educativo"));
        categoryRepo.saveAll(categories);

        System.out.println("🚀 Insertando " + seedSize + " productos (en batches) ...");
        int batchSize = 1000;
        List<Product> batch = new ArrayList<>(batchSize);
        for (int i = 0; i < seedSize; i++) {
            Category c = categories.get(rnd.nextInt(categories.size()));
            Product p = new Product(
                    null,
                    faker.commerce().productName(),
                    faker.lorem().sentence(6),
                    Math.round((10 + rnd.nextDouble() * 990) * 100.0) / 100.0, // price
                    rnd.nextInt(500), // stock
                    c
            );
            batch.add(p);
            if (batch.size() >= batchSize) {
                productRepo.saveAll(batch);
                batch.clear();
                System.out.println("Inserted " + (i+1) + "/" + seedSize);
            }
        }
        if (!batch.isEmpty()) {
            productRepo.saveAll(batch);
            System.out.println("Inserted " + seedSize + "/" + seedSize);
        }

        System.out.println("✅ Seed completado.");
    }
}
