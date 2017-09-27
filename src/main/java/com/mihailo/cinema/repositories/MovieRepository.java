package com.mihailo.cinema.repositories;

import com.mihailo.cinema.model.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

import static com.mihailo.cinema.common.ApiConstants.Movie.*;
import static com.mihailo.cinema.common.ApiConstants.Date.*;

@RepositoryRestResource(collectionResourceRel = REL, path = PATH)
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    @RestResource(path = SEARCH_TITLE, rel = REL_TITLE)
    List<Movie> findByTitle(@Param(PARAM_TITLE) String title);

    @RestResource(path = SEARCH_UPCOMING, rel = REL_UPCOMING)
    List<Movie> findByPremiereDateAfter(@DateTimeFormat(pattern = FORMAT) @Param(PARAM_DATE) Date date);

    @RestResource(path = SEARCH_UPCOMING_RANGE, rel = REL_UPCOMING_RANGE)
    List<Movie> findByPremiereDateAfterAndPremiereDateBefore(
            @DateTimeFormat(pattern = FORMAT) @Param(PARAM_DATE_FROM) Date fromDate,
            @DateTimeFormat(pattern = FORMAT) @Param(PARAM_DATE_TO) Date toDate
    );

    @Override
    Movie save(Movie movie);

    @Override
    Iterable save(Iterable col);

    @Override
    void delete(Long id);

    @Override
    void delete(Movie movie);

    @Override
    void deleteAll();

}
