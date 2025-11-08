package com.college.skillbridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SchemaBootstrapRunner implements CommandLineRunner {

    private final boolean schemaBootstrapEnabled;

    public SchemaBootstrapRunner(@Value("${app.schema-bootstrap:false}") boolean schemaBootstrapEnabled) {
        this.schemaBootstrapEnabled = schemaBootstrapEnabled;
    }

    @Override
    public void run(String... args) {
        if (schemaBootstrapEnabled) {
            System.out.println("Schema bootstrap flag detected. Shutting down after initialization.");
            System.exit(0);
        }
    }
}
