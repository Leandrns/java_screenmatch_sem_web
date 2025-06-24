package br.com.alura.screenmatch.model;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getImbdRating() {
        return imbdRating;
    }

    public void setImbdRating(Double imbdRating) {
        this.imbdRating = imbdRating;
    }

    public Categoria getGenre() {
        return genre;
    }

    public void setGenre(Categoria genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "genre=" + genre +
                ", title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", imbdRating=" + imbdRating +
                ", actors='" + actors + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", poster='" + poster + '\'';
    }
}
