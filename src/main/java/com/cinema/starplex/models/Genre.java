package com.cinema.starplex.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Genre {
    private IntegerProperty id;
    private StringProperty name;

    public Genre(){}

    public Genre(IntegerProperty id, StringProperty name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
