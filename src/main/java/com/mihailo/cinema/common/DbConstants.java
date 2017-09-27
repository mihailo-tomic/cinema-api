package com.mihailo.cinema.common;

public class DbConstants {

    public static class  Movie {

        public static final String TABLE_NAME = "movie";

        public static final String ID = "id";

        public static final String TITLE            = "title";
        public static final String ORIGINAL_TITLE   = "original_title";
        public static final String DURATION         = "duration";
        public static final String DIRECTOR         = "director";

        public static final String PREMIERE_DATE    = "premiere_date";

    }

    public static class Genre {

        public static final String TABLE_NAME = "genre";

        public static final String ID   = "id";
        public static final String NAME = "name";

    }

    public static class Actor {

        public static final String TABLE_NAME = "actor";

        public static final String ID = "id";

        public static final String NAME = "name";
        public static final String CHARACTER = "character";

    }

    public static class MovieHasGenre {

        public static final String TABLE_NAME = "movie_has_genre";

        public static final String MOVIE_ID = "movie_id";
        public static final String GENRE_ID = "genre_id";

    }

    public static class MovieHasActor {

        public static final String TABLE_NAME = "movie_has_actor";

        public static final String MOVIE_ID = "movie_id";
        public static final String ACTOR_ID = "actor_id";

    }

}
