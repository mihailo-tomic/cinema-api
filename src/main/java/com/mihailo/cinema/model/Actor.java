package com.mihailo.cinema.model;

import com.mihailo.cinema.common.DbConstants;

import javax.persistence.*;

@Entity(name = DbConstants.Actor.TABLE_NAME)
public class Actor {

    @Id
    @Column(name = DbConstants.Actor.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = DbConstants.Actor.NAME)
    private String name;
    @Column(name = DbConstants.Actor.CHARACTER)
    private String character;

    public Actor() {}

    public Actor(String name, String character) {
        this(null, name, character);
    }

    public Actor(Long id, String name, String character) {
        this.id = id;
        this.name = name;
        this.character = character;
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

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", character='" + character + '\'' +
                '}';
    }

}
