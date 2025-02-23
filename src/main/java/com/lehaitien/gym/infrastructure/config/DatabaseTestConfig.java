package com.lehaitien.gym.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTestConfig  implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void run(String... args) throws Exception {
        try {
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            System.out.println("✅ Kết nối MySQL Cloud Aiven thành công! Kết quả: " + result);
        } catch (Exception e) {
            System.err.println("❌ Kết nối MySQL Cloud thất bại!");
            e.printStackTrace();
        }
    }
}
