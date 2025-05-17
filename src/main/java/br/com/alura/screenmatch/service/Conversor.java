package br.com.alura.screenmatch.service;

public interface Conversor {
    <T> T obterDados(String json, Class<T> classe);
}
