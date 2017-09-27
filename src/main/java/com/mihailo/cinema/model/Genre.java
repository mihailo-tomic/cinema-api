package com.mihailo.cinema.model;

import com.mihailo.cinema.common.DbConstants;

import javax.persistence.*;

@Entity(name = DbConstants.Genre.TABLE_NAME)
public class Genre {

    @Id
    @Column(name = DbConstants.Genre.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = DbConstants.Genre.NAME)
    private String name;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
