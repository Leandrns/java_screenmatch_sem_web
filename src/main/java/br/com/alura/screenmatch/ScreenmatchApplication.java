package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		ConverteDados conversor = new ConverteDados();

		String json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&apikey=6fe24967");
		DadosSerie breakingBad = conversor.obterDados(json, DadosSerie.class);
		System.out.println(breakingBad);

		List<DadosTemporada> listaTemporadas = new ArrayList<>();
		for (int i = 1; i <= breakingBad.totalSeasons(); i++) {
			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&Season=" + i + "&apikey=6fe24967");
			DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
			listaTemporadas.add(temporada);
		}
		listaTemporadas.forEach(System.out::println);



//		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&Season=1&episode=2&apikey=6fe24967");
//		DadosEpisodio breakingBadEp2 = conversor.obterDados(json, DadosEpisodio.class);
//		System.out.println(breakingBadEp2);
//
//		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&Season=1&apikey=6fe24967");
//		DadosTemporada breakingBadTemp1 = conversor.obterDados(json, DadosTemporada.class);
//		System.out.println(breakingBadTemp1);
	}
}
