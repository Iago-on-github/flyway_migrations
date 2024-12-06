package br.com.migrations_flyway.models.Dtos;

import java.time.LocalDate;

public record BookDto(String title,
                      String author,
                      Double price,
                      String launch_date) {
}
