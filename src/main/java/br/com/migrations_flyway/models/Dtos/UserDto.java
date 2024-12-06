package br.com.migrations_flyway.models.Dtos;

public record UserDto(String name, int age, String cpf, Boolean enabled) {
}
