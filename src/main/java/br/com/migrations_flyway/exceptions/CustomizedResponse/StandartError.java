package br.com.migrations_flyway.exceptions.CustomizedResponse;

import java.time.LocalDate;

public class StandartError {
    private LocalDate date;
    private String message;
    private String description;

    public StandartError(LocalDate date, String message, String description) {
        this.date = date;
        this.message = message;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
