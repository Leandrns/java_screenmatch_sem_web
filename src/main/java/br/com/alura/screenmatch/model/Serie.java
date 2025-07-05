package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer totalSeasons;
    private Double imdbRating;
    @Enumerated(EnumType.STRING)
    private Categoria genre;
    private String actors;
    private String sinopse;
    private String poster;

    // Relação um para muitos: mapeado para a coluna serie de Episodio, ou seja, surge uma chave estrangeira de Serie na tabela Episodio
    // CascadeType.ALL significa que todas as operações (persist, merge, remove, refresh, detach) serão propagadas para Episodio
    // FetchType.EAGER significa que os episódios serão carregados junto com a série, ou seja, quando uma série for buscada, seus episódios também serão carregados
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie() {}

    public Serie(DadosSerie dadosSerie) {
        this.title = dadosSerie.title();
        this.totalSeasons = dadosSerie.totalSeasons();
        this.imdbRating = OptionalDouble.of(Double.parseDouble(dadosSerie.imbdRating())).orElse(0);
        this.genre = Categoria.fromString(dadosSerie.genre().split(",")[0].trim());
        this.actors = dadosSerie.actors();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
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

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
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

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        // Episodio possui um atributo Serie, então é necessário setar a série em cada episódio, para que haja a relação bidirecional e o banco possa entender qual é a chave estrangeira
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return "genre=" + genre +
                ", title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", imbdRating=" + imdbRating +
                ", actors='" + actors + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", poster='" + poster + '\'' +
                ", episodios='" + episodios + '\'';
    }
}
