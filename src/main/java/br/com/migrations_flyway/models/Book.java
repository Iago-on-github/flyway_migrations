package br.com.migrations_flyway.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "books")
@JsonPropertyOrder(value = {"key", "title", "author", "price", "launch_date"})
public class Book extends RepresentationModel<Book> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Integer key;
    private String title;
    private String author;
    private Double price;
    private String launch_date;

    public Book() {
    }

    public Book(Integer key, String title, String author, Double price, String launch_date) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.price = price;
        this.launch_date = launch_date;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(String launch_date) {
        this.launch_date = launch_date;
    }
}
