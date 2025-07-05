package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTitleContainingIgnoreCase(String nomeSerie);

    List<Serie> findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByImdbRatingDesc();

    List<Serie> findByGenre(Categoria categoria);

    List<Serie> findByTotalSeasonsLessThanEqualAndImdbRatingGreaterThanEqual(Integer totalTemporadas, Double avaliacao);
}
