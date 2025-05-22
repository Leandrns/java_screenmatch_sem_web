package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6fe24967";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.println("Digite o nome de uma série:");
        var serie = scanner.nextLine();
        serie = serie.replace(" ", "+");
        String json = consumoAPI.obterDados(ENDERECO + serie + API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> listaTemporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalSeasons(); i++) {
            json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + serie + "&Season=" + i + "&apikey=6fe24967");
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            listaTemporadas.add(temporada);
        }
        listaTemporadas.forEach(System.out::println);

        // função lambda --- semelhante à arrow function do javascript (funções anônimas)
        listaTemporadas.forEach(t -> {
            System.out.println("\nTemporada " + t.numTemp());
            t.episodios().forEach(e -> System.out.println(e.titulo()));
        });

        List<DadosEpisodio> dadosEpisodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .toList();

        System.out.println("\nTop 5 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numTemp(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);


    }
}
