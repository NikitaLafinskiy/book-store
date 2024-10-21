package com.bookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class BookstoreMySqlContainer extends MySQLContainer<BookstoreMySqlContainer> {
    private static final String IMAGE_VERSION = "mysql:9.0.0";
    private static final String SPRING_DATASOURCE_PASSWORD = "SPRING_DATASOURCE_PASSWORD";
    private static final String SPRING_DATASOURCE_USERNAME = "SPRING_DATASOURCE_USERNAME";
    private static final String SPRING_DATASOURCE_URL = "SPRING_DATASOURCE_URL";

    private static BookstoreMySqlContainer container;

    private BookstoreMySqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BookstoreMySqlContainer getInstance() {
        if (container == null) {
            container = new BookstoreMySqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty(SPRING_DATASOURCE_URL, container.getJdbcUrl());
        System.setProperty(SPRING_DATASOURCE_USERNAME, container.getUsername());
        System.setProperty(SPRING_DATASOURCE_PASSWORD, container.getPassword());
    }
}
