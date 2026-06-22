package com.example.backEnd.Service;

import com.example.backEnd.DTO.clima.ClimaResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Map;

@Service
public class ClimaService {

    private final WebClient webClient;
    private final String apiKey;
    private final double lat;
    private final double lon;
    private final int timeoutMs;

    public ClimaService(
            @Value("${clima.openweathermap.base-url}") String baseUrl,
            @Value("${clima.openweathermap.api-key}") String apiKey,
            @Value("${clima.openweathermap.lat}") double lat,
            @Value("${clima.openweathermap.lon}") double lon,
            @Value("${clima.openweathermap.timeout-ms}") int timeoutMs) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.lat = lat;
        this.lon = lon;
        this.timeoutMs = timeoutMs;
    }

    @SuppressWarnings("unchecked")
    public ClimaResponseDTO getClima(String fecha, String hora) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("lang", "es")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            if (response == null) {
                return fallback();
            }

            var lista = (java.util.List<Map<String, Object>>) response.get("list");
            if (lista == null || lista.isEmpty()) {
                return fallback();
            }

            String targetDt = fecha + " " + hora;
            Map<String, Object> slot = lista.stream()
                    .min((a, b) -> {
                        String dtA = (String) a.get("dt_txt");
                        String dtB = (String) b.get("dt_txt");
                        return Math.abs(dtA.compareTo(targetDt)) - Math.abs(dtB.compareTo(targetDt));
                    })
                    .orElse(lista.get(0));

            var weather = ((java.util.List<Map<String, Object>>) slot.get("weather")).get(0);
            var main = (Map<String, Object>) slot.get("main");
            String descripcion = (String) weather.get("description");
            String icono = (String) weather.get("icon");
            double temp = ((Number) main.get("temp")).doubleValue();

            return new ClimaResponseDTO(true, descripcion, temp, icono);

        } catch (WebClientException | IllegalStateException e) {
            return fallback();
        }
    }

    private ClimaResponseDTO fallback() {
        return new ClimaResponseDTO(false, "Servicio de clima no disponible", null, null);
    }
}
