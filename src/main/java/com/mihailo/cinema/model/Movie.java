package com.mihailo.cinema.model;

import com.mihailo.cinema.common.DbConstants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = DbConstants.Movie.TABLE_NAME)
public class Movie {

    @Id
    @Column(name = DbConstants.Movie.ID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = DbConstants.Movie.TITLE)
    private String title;
    @Column(name = DbConstants.Movie.ORIGINAL_TITLE)
    private String originalTitle;

    @Column(name = DbConstants.Movie.DURATION)
    private Integer duration;

    @Column(name = DbConstants.Movie.DIRECTOR)
    private String director;

    @Temporal(TemporalType.DATE)
    @Column(name = DbConstants.Movie.PREMIERE_DATE)
    private Date premiereDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = DbConstants.MovieHasGenre.TABLE_NAME,
            joinColumns = {
                @JoinColumn(name = DbConstants.MovieHasGenre.MOVIE_ID)
            }, inverseJoinColumns = {
                @JoinColumn(name = DbConstants.MovieHasGenre.GENRE_ID)
            }
    )
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = DbConstants.MovieHasActor.TABLE_NAME,
            joinColumns = {
                @JoinColumn(name = DbConstants.MovieHasActor.MOVIE_ID)
            }, inverseJoinColumns = {
                @JoinColumn(name = DbConstants.MovieHasActor.ACTOR_ID)
            }
    )
    private List<Actor> actors;

    public Movie() {}

    public Movie(Long id, String title, String originalTitle, Integer duration, String director, Date premiereDate, List<Genre> genres, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.duration = duration;
        this.director = director;
        this.premiereDate = premiereDate;
        this.genres = genres;
        this.actors = actors;
    }

    public Movie(String title, String originalTitle, Integer duration, String director, Date premiereDate, List<Genre> genres, List<Actor> actors) {
        this(null, title, originalTitle, duration, director, premiereDate, genres, actors);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(Date premiereDate) {
        this.premiereDate = premiereDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", duration=" + duration +
                ", director='" + director + '\'' +
                ", premiereDate=" + premiereDate +
                ", genres=" + genres +
                ", actors=" + actors +
                '}';
    }

}
