package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		String json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&apikey=6fe24967");

		ConverteDados conversor = new ConverteDados();

		DadosSerie breakingBad = conversor.obterDados(json, DadosSerie.class);
		System.out.println(breakingBad);

		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=breaking+bad&Season=1&episode=2&apikey=6fe24967");
		DadosEpisodio breakingBadEp2 = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(breakingBadEp2);
	}
}
