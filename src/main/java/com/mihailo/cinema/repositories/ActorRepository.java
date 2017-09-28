package com.mihailo.cinema.repositories;

import com.mihailo.cinema.model.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = true)
public interface ActorRepository extends CrudRepository<Actor, Long> {}
