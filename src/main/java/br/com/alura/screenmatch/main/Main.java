package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    }
}
