package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String nomeSerie);

    List<Serie> findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByImdbRatingDesc();

    List<Serie> findByGenre(Categoria categoria);

    List<Serie> findByTotalSeasonsLessThanEqualAndImdbRatingGreaterThanEqual(Integer totalTemporadas, Double avaliacao);

    // Usando JPQL
    // Uma consulta JPQL é escrita baseada nas entidades e atributos do Java.
    // Isso garante que a query funcione em qualquer banco, contribuindo para o desacoplamento e portabilidade da aplicação.
    // Para indicar o uso de parâmetros, é necessário colocar ":" antes do nome do parâmetro na query.
    // A JPQL deixa as consultas mais legíveis, uma vez que algumas Derived Queries ficam enormes
    @Query("SELECT s FROM Serie s WHERE s.totalSeasons <= :totalTemporadas AND s.imdbRating >= :avaliacao ")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
