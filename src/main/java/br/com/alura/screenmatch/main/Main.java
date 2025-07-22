package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fe24967";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private SerieRepository serieRepository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;

    public Main(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por título
                5 - Buscar séries por ator
                6 - Top 5 séries
                7 - Buscar séries por categoria
                8 - Buscar séries por quatidade máxima de temporadas
                9 - Buscar episódios por trecho
                10 - Top 5 episódios por série
                
                0 - Sair
                """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorQuantidadeMaxTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void listarSeriesBuscadas() {
        // buscando todas as séries do banco de dados
        series = serieRepository.findAll();

        // ordenando as séries por Genre (gênero) e exibindo na tela
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        serieRepository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalSeasons(); i++) {
                var json = consumoAPI.obterDados(ENDERECO + serieEncontrada.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numTemp(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada.");
        }

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        serieBuscada = serieRepository.findByTitleContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Série encontrada!\nVeja os dados:" + serieBuscada.get());
        } else {
            System.out.println("Nenhuma série encontrada.");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome para busca:");
        var nomeAtor = scanner.nextLine();
        System.out.println("Digite a avaliação mínima da série:");
        var avaliacao = scanner.nextDouble();

        List<Serie> seriesEncontradas = serieRepository.findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(nomeAtor, avaliacao);

        System.out.println("Séries encontradas:");
        seriesEncontradas.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = serieRepository.findTop5ByOrderByImdbRatingDesc();
        serieTop.forEach(s -> System.out.println(s.getTitle() + " - " + "Avaliação: " + s.getImdbRating()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Digite a categoria/gênero que está buscando:");
        var nomeCategoria = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
        List<Serie> seriesEncontradas = serieRepository.findByGenre(categoria);
        System.out.println("Séries de " + nomeCategoria + " encontradas:");
        seriesEncontradas.forEach(System.out::println);
    }

    private void buscarSeriesPorQuantidadeMaxTemporadas() {
        System.out.println("Digite um número máximo de temporadas:");
        var maximoTemporadas = scanner.nextInt();
        System.out.println("Digite uma avaliação mínima:");
        var minAvaliacao = scanner.nextDouble();
        List<Serie> seriesEncontradas = serieRepository.seriesPorTemporadaEAvaliacao(maximoTemporadas, minAvaliacao);

        System.out.println("Séries encontradas:");
        seriesEncontradas.forEach(System.out::println);
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio para busca:");
        var trechoEpisodio = scanner.nextLine();
        List<Episodio> episodiosEcontrados = serieRepository.episodiosPorTrecho(trechoEpisodio);
        episodiosEcontrados.forEach(e -> System.out.printf("Série: %s  Temporada %d - Episódio: %s\n", e.getSerie().getTitle(), e.getTemporada(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = serieRepository.topEpisodiosPorSerie(serie);
            System.out.println("Top 5 episódios de " + serie.getTitle());
            topEpisodios.forEach(e -> System.out.printf("Temporada %d - Episódio: %s   Avaliação: %.1f\n", e.getTemporada(), e.getTitulo(), e.getAvaliacao()));
        }
    }
}
