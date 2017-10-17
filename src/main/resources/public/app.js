(function() {

    var app = angular.module('cinema', []);

    app.controller('CinemaController', function() {
        this.movies = movies;
    });

    var movies = [
        {
            title: 'Gospodar Prstenova: Druzina Prstena',
            originalTitle: 'The Lord of the Rings: The Fellowship of the Ring',
            duration: '178',
            rating: 'PG',
            director: 'Peter Jackson',
            premiereDate: '2001-12-19',
            genres: [
                "drama", "adventure", "fantasy"
            ]
        }, {
            title: 'Gospodar Prstenova: Dve Kule',
            originalTitle: 'The Lord of the Rings: Two Towers',
            duration: '179',
            rating: '12A',
            director: 'Peter Jackson',
            premiereDate: '2002-12-18',
            genres: [
                "drama", "adventure", "fantasy"
            ]
        }, {
            title: "Gospodar Prstenova: Povratak Kralja",
            originalTitle: "Lord of the Rings: The Return of the King",
            duration: "201",
            rating: '12A',
            director: "Peter Jackson",
            premiereDate: "2003-12-17",
            genres: [
                "drama", "adventure", "fantasy"
            ]
        }
    ];

})();