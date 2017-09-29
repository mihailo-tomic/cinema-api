package com.mihailo.cinema.common;

public class ApiConstants {

    public static final String BASE_PATH = "/cinema/api/";

    public static class Date {

        public static final String FORMAT = "yyyy-MM-dd";

    }

    public static class Movie {

        public static final String PATH = "movies";
        public static final String REL = "movies";

        public static final String PARAM_TITLE = "title";

        public static final String PARAM_DATE = "date";

        public static final String PARAM_DATE_FROM = "fromDate";
        public static final String PARAM_DATE_TO = "toDate";

        public static final String SEARCH_TITLE = "/title";
        public static final String REL_TITLE = "titleSearch";

        public static final String SEARCH_UPCOMING = "/upcoming";
        public static final String REL_UPCOMING = "upcomingSearch";

        public static final String SEARCH_UPCOMING_RANGE = "/upcomingRange";
        public static final String REL_UPCOMING_RANGE = "upcomingRangeSearch";

        public static final String MOVIE_PATH = BASE_PATH + PATH;
        public static final String SINGLE_MOVIE_PATH = MOVIE_PATH + "/{movieId}";
        public static final String GENRES_PATH = SINGLE_MOVIE_PATH + "/genres";
        public static final String ACTORS_PATH = SINGLE_MOVIE_PATH + "/actors";

    }

}
