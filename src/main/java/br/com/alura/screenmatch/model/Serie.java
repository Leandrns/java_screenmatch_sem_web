package br.com.alura.screenmatch.model;

import java.util.Optional;
import java.util.OptionalDouble;

public class Serie {
    private String title;
    private Integer totalSeasons;
    private Double imbdRating;
    private Categoria genre;
    private String actors;
    private String sinopse;
    private String poster;

    public Serie(DadosSerie dadosSerie) {
        this.title = dadosSerie.title();
        this.totalSeasons = dadosSerie.totalSeasons();
        this.imbdRating = OptionalDouble.of(Double.parseDouble(dadosSerie.imbdRating())).orElse(0);
        this.genre = Categoria.fromString(dadosSerie.genre().split(",")[0].trim());
        this.actors = dadosSerie.actors();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
    }
}
