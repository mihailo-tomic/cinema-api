package com.mihailo.cinema;

import com.mihailo.cinema.common.ApiConstants;
import com.mihailo.cinema.model.Actor;
import com.mihailo.cinema.model.Genre;
import com.mihailo.cinema.model.Movie;
import com.mihailo.cinema.repositories.ActorRepository;
import com.mihailo.cinema.repositories.GenreRepository;
import com.mihailo.cinema.repositories.MovieRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
public class MovieRestEndpointTest {

    private MediaType contentTypeApplicationJson = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MediaType contentTypeUriList = new MediaType("text", "uri-list", Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ActorRepository actorRepository;

    private List<Genre> genres;
    private List<Actor> actors;

    private Movie movie;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    private String json(Object o) throws IOException {

        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }

    @Before
    public void beforeEveryTest(){

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.genreRepository.deleteAll();
        this.actorRepository.deleteAll();
        this.movieRepository.deleteAll();

        this.genres = new ArrayList<>();
        this.genres.add(genreRepository.save(new Genre("action")));
        this.genres.add(genreRepository.save(new Genre("drama")));
        this.genres.add(genreRepository.save(new Genre("horror")));
        this.genres.add(genreRepository.save(new Genre("adventure")));
        this.genres.add(genreRepository.save(new Genre("fantasy")));

        this.actors = new ArrayList<>();
        this.actors.add(actorRepository.save(new Actor("Elijah Wood","Frodo")));
        this.actors.add(actorRepository.save(new Actor("Orlando Bloom", "Legolas")));
        this.actors.add(actorRepository.save(new Actor("Viggo Mortensen", "Aragorn")));
        this.actors.add(actorRepository.save(new Actor("Ian McKellen", "Gandalf")));

        this.movie = movieRepository.save(
                new Movie(
                    "Gospodar Prstenova: Druzina Prstena",
                    "The Lord of the Rings: The Fellowship of the Ring",
                    178,
                    "Peter Jackson",
                    new Date(
                            OffsetDateTime.of(LocalDateTime.of(2001, 12, 19, 0, 0), ZoneOffset.ofHours(0)).toEpochSecond()*1000),
                    Arrays.asList(this.genres.get(1), this.genres.get(3), this.genres.get(4)),
                    Arrays.asList(this.actors.get(0), this.actors.get(1))
                )
        );

    }

    @After
    public void afterEveryTest() {

        this.genreRepository.deleteAll();
        this.actorRepository.deleteAll();
        this.movieRepository.deleteAll();

    }

    @Test
    public void testCreateMovie() throws Exception {

        String movieJson = json(
                new Movie(
                        "Gospodar Prstenova: Dve Kule",
                        "The Lord of the Rings: Two Towers",
                        179,
                        "Peter Jackson",
                        new Date(
                                OffsetDateTime.of(LocalDateTime.of(2002, 12, 18, 0, 0), ZoneOffset.ofHours(0))
                                .toEpochSecond()*1000
                        ),
                        Arrays.asList(
                                new Genre("drama"),
                                new Genre("adventure"),
                                new Genre("fantasy")),
                        Arrays.asList(
                                new Actor("Ian McKellen", "Gandalf"),
                                new Actor("Viggo Mortensen", "Aragorn"))
                )
        );

        System.out.println(movieJson);

        this.mockMvc.perform(
                post(ApiConstants.Movie.MOVIE_PATH)
                        .contentType(contentTypeApplicationJson)
                        .content(movieJson)
        ).andExpect(
                status().isCreated()
        ).andDo(
                print()
        );

    }

    @Test
    public void testGetAllMovies() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.MOVIE_PATH)
        ).andExpect(
                jsonPath("$._embedded.movies", hasSize(1))
        ).andExpect(
                jsonPath("$._embedded.movies[0].title", is(movie.getTitle()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testGetSingleMovie() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SINGLE_MOVIE_PATH, this.movie.getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.title", is(this.movie.getTitle()))
        ).andExpect(
                jsonPath("$.originalTitle", is(this.movie.getOriginalTitle()))
        ).andExpect(
                jsonPath("$.duration", is(this.movie.getDuration()))
        ).andDo(
                print()
        );
    }

    @Test
    public void testGetSingleMovieWhenMovieDoesNotExist() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SINGLE_MOVIE_PATH, -1)
        ).andExpect(
                status().isNotFound()
        ).andDo(
                print()
        );

    }

    @Test
    public void testUpdateMovie() throws Exception {

        Integer newDuration = 125;

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle(movie.getTitle());
        updatedMovie.setOriginalTitle(movie.getOriginalTitle());
        updatedMovie.setDuration(newDuration);
        updatedMovie.setDirector(movie.getDirector());
        updatedMovie.setPremiereDate(movie.getPremiereDate());
        updatedMovie.setActors(movie.getActors());
        updatedMovie.setGenres(movie.getGenres());

        String movieJson = json(updatedMovie);

        this.mockMvc.perform(
                put(ApiConstants.Movie.SINGLE_MOVIE_PATH, movie.getId()).contentType(contentTypeApplicationJson).content(movieJson)
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get(ApiConstants.Movie.SINGLE_MOVIE_PATH, movie.getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.duration", is(newDuration))
        ).andDo(
                print()
        );

    }

    @Test
    public void testSetGenres() throws Exception {

        List<String> newGenres = new LinkedList<>();
        newGenres.add("http://localhost:8080/cinema/api/genres/" + genres.get(0).getId());  // action
        newGenres.add("http://localhost:8080/cinema/api/genres/" + genres.get(1).getId());  // drama
        newGenres.add("http://localhost:8080/cinema/api/genres/" + genres.get(3).getId());  // adventure

        this.mockMvc.perform(
                put(ApiConstants.Movie.GENRES_PATH, movie.getId())
                    .contentType(contentTypeUriList)
                    .content(String.join("\n", newGenres))
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get(ApiConstants.Movie.GENRES_PATH, movie.getId())
        ).andExpect(
                jsonPath("$._embedded.genres", hasSize(3))
        ).andExpect(
                jsonPath("$..name", hasItems(this.genres.get(0).getName(), this.genres.get(1).getName(), this.genres.get(3).getName()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testAddGenres() throws Exception {

        List<String> newGenres = new LinkedList<>();
        newGenres.add("http://localhost:8080/cinema/api/genres/" + genres.get(0).getId());  // action
        newGenres.add("http://localhost:8080/cinema/api/genres/" + genres.get(2).getId());  // horror

        this.mockMvc.perform(
                patch(ApiConstants.Movie.GENRES_PATH, movie.getId())
                        .contentType(contentTypeUriList)
                        .content(String.join("\n", newGenres))
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get(ApiConstants.Movie.GENRES_PATH, movie.getId())
        ).andExpect(
                jsonPath("$._embedded.genres", hasSize(5))
        ).andExpect(
                jsonPath("$..name", hasItems(this.genres.get(0).getName(), this.genres.get(1).getName(), this.genres.get(2).getName(), this.genres.get(3).getName(), this.genres.get(4).getName()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testSetActors() throws Exception {

        List<String> newActors = new LinkedList<>();
        newActors.add("http://localhost:8080/cinema/api/actors/" + actors.get(0).getId());
        newActors.add("http://localhost:8080/cinema/api/actors/" + actors.get(1).getId());
        newActors.add("http://localhost:8080/cinema/api/actors/" + actors.get(3).getId());

        this.mockMvc.perform(
                put(ApiConstants.Movie.ACTORS_PATH, movie.getId())
                        .contentType(contentTypeUriList)
                        .content(String.join("\n", newActors))
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get(ApiConstants.Movie.ACTORS_PATH, movie.getId())
        ).andExpect(
                jsonPath("$._embedded.actors", hasSize(3))
        ).andExpect(
                jsonPath("$..name", hasItems(this.actors.get(0).getName(), this.actors.get(1).getName(), this.actors.get(3).getName()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testAddActors() throws Exception {

        List<String> newActors = new LinkedList<>();
        newActors.add("http://localhost:8080/cinema/api/actors/" + actors.get(2).getId());
        newActors.add("http://localhost:8080/cinema/api/actors/" + actors.get(3).getId());

        this.mockMvc.perform(
                patch(ApiConstants.Movie.ACTORS_PATH, movie.getId())
                        .contentType(contentTypeUriList)
                        .content(String.join("\n", newActors))
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get(ApiConstants.Movie.ACTORS_PATH, movie.getId())
        ).andExpect(
                jsonPath("$._embedded.actors", hasSize(4))
        ).andExpect(
                jsonPath("$..name", hasItems(this.actors.get(0).getName(), this.actors.get(1).getName(), this.actors.get(2).getName(), this.actors.get(3).getName()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testDeleteMovie() throws Exception {

        this.mockMvc.perform(
                delete(ApiConstants.Movie.SINGLE_MOVIE_PATH, movie.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

    }

    @Test
    public void testDeleteMovieWhenMovieDoesNotExist() throws Exception {

        this.mockMvc.perform(
                delete(ApiConstants.Movie.SINGLE_MOVIE_PATH, -1)
        ).andExpect(
                status().isNotFound()
        ).andDo(
                print()
        );

    }

    @Test
    public void testTitleSearchWhenMovieExists() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_TITLE_PATH, movie.getTitle())
        ).andExpect(
                jsonPath("$._embedded.movies[0].title", is(movie.getTitle()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testTitleSearchWhenMovieDoesNotExist() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_TITLE_PATH, -1)
        ).andExpect(
                jsonPath("$._embedded.movies", hasSize(0))
        ).andDo(
                print()
        );

    }

    @Test
    public void testUpcomingSearchWhenMovieExists() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_UPCOMING_PATH, "2001-12-01")
        ).andExpect(
                jsonPath("$._embedded.movies[0].title", is(movie.getTitle()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testUpcomingSearchWhenMovieDoesNotExist() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_UPCOMING_PATH, "2005-01-01")
        ).andExpect(
                jsonPath("$._embedded.movies", hasSize(0))
        ).andDo(
                print()
        );

    }

    @Test
    public void testUpcomingRangeSearchWhenMovieExists() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_UPCOMING_RANGE_PATH, "2001-12-01", "2001-12-31")
        ).andExpect(
                jsonPath("$._embedded.movies", hasSize(1))
        ).andExpect(
                jsonPath("$._embedded.movies[0].title", is(movie.getTitle()))
        ).andDo(
                print()
        );

    }

    @Test
    public void testUpcomingRangeSearchWhenMovieDoesNotExist() throws Exception {

        this.mockMvc.perform(
                get(ApiConstants.Movie.SEARCH_UPCOMING_RANGE_PATH, "2001-12-21", "2001-12-31")
        ).andExpect(
                jsonPath("$._embedded.movies", hasSize(0))
        ).andDo(
                print()
        );

    }

}
