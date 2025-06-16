package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record para armazenar os dados da série vindos do JSON
 * @param title título da série
 * @param totalSeasons número de temporadas
 * @param imbdRating nota imdb da série
 */

@JsonIgnoreProperties(ignoreUnknown = true) // Para não tentar converter propriedades que não estão especificadas
public record DadosSerie(@JsonAlias("Title") String title,
                         @JsonAlias("totalSeasons") Integer totalSeasons,
                         @JsonAlias("imdbRating") String imbdRating,
                         @JsonAlias("Genre") String genre,
                         @JsonAlias("Actors") String actors,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("Poster") String poster) {
}


// @JsonAlias: define qual é a propriedade do json que o atributo se refere.

