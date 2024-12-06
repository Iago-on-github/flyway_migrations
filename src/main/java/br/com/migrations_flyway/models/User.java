package br.com.migrations_flyway.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;
@Entity
@Table(name="users_data")
@JsonPropertyOrder({"key", "name", "cpf", "age", "enabled"})
public class User extends RepresentationModel<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private UUID key;
    private String name;
    private int age;
    private String cpf;
    @Column(nullable = false)
    private Boolean enabled;

    public User() {
    }

    public User(UUID key, String name, int age, String cpf, Boolean enabled) {
        this.key = key;
        this.name = name;
        this.age = age;
        this.cpf = cpf;
        this.enabled = enabled;
    }

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
