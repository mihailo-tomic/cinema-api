package com.mihailo.cinema.repositories;

import com.mihailo.cinema.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface GenreRepository extends CrudRepository<Genre, Long> {

    Genre findByName(String name);

}
