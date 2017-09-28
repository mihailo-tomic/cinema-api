package com.mihailo.cinema;

import com.mihailo.cinema.common.ApiConstants;
import com.mihailo.cinema.model.Actor;
import com.mihailo.cinema.model.Genre;
import com.mihailo.cinema.model.Movie;
import com.mihailo.cinema.repositories.ActorRepository;
import com.mihailo.cinema.repositories.GenreRepository;
import com.mihailo.cinema.repositories.MovieRepository;
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

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

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
                post("/cinema/api/" + ApiConstants.Movie.PATH).contentType(contentType).content(movieJson)
        ).andExpect(
                status().isCreated()
        ).andDo(
                print()
        );

    }

    @Test
    public void testGetSingleMovie() throws Exception {

        this.mockMvc.perform(
                get("/cinema/api/" + ApiConstants.Movie.PATH + "/" + this.movie.getId())
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$.title", is(this.movie.getTitle()))
        ).andExpect(jsonPath("$.originalTitle", is(this.movie.getOriginalTitle()))
        ).andExpect(jsonPath("$.duration", is(this.movie.getDuration()))
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
                patch("/cinema/api/" + ApiConstants.Movie.PATH + "/" +movie.getId()).contentType(contentType).content(movieJson)
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

        this.mockMvc.perform(
                get("/cinema/api/" + ApiConstants.Movie.PATH + "/" + movie.getId())
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.duration", is(newDuration))
        ).andDo(
                print()
        );

    }

    @Test
    public void testDeleteMovie() throws Exception {

        this.mockMvc.perform(
                delete("/cinema/api/" + ApiConstants.Movie.PATH + "/" + movie.getId())
        ).andExpect(
                status().is2xxSuccessful()
        ).andDo(
                print()
        );

    }

}
