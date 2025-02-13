package com.showcaseScore.movieApp.util;

import com.showcaseScore.movieApp.dtos.MovieDTO;
import com.showcaseScore.movieApp.exception.ApiCallException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class CallApi {

    private final String OMDBUrl;
    private final String TMDBUrlImdb;
    private final String TMDBUrlPopular;
    private final String TMDBUrlTopRated;
    private final String TMDBUrlExternalLinks;
    private final String TMDBApiKey;
    private final String OMDBApiKey;
    private final RestTemplate restTemplate;

    @Autowired
    public CallApi(
            @Value("${OMDB.url}") String OMDBUrl,
            @Value("${TMDB.url.imdb}") String TMDBUrlImdb,
            @Value("${TMDB.url.popular}") String TMDBUrlPopular,
            @Value("${TMDB.url.top-rated}") String TMDBUrlTopRated,
            @Value("${TMDB.url.external-links}") String TMDBUrlExternalLinks,
            @Value("${TMDB.api.key}") String TMDBApiKey,
            @Value("${OMDB.api.key}") String OMDBApiKey,
            RestTemplate restTemplate) {
        this.OMDBUrl = OMDBUrl;
        this.TMDBUrlImdb = TMDBUrlImdb;
        this.TMDBUrlPopular = TMDBUrlPopular;
        this.TMDBUrlTopRated = TMDBUrlTopRated;
        this.TMDBUrlExternalLinks = TMDBUrlExternalLinks;
        this.TMDBApiKey = TMDBApiKey;
        this.OMDBApiKey = OMDBApiKey;
        this.restTemplate = restTemplate;
    }

    public MovieDTO getMovieFromOMDB(String imdbId) {
        log.info("Fetching movie data from OMDB for IMDb ID: {}", imdbId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(OMDBUrl)
                .queryParam("i", imdbId)
                .queryParam("apikey", OMDBApiKey)
                .queryParam("plot", "full");

        MovieDTO movieDto = executeHttpGetRequest(builder.toUriString(), MovieDTO.class);
        if (movieDto == null) {
            log.error("Failed to fetch movie data from OMDB for IMDb ID: {}", imdbId);
            throw new ApiCallException("Failed to fetch movie data from OMDB for IMDb ID: " + imdbId);
        }
        return movieDto;
    }

    public String getTrailerFromTMDB(String imdbId, String urlTMDBTrailer) {
        Integer movieId = getMovieIdFromTMDB(imdbId);
        if (movieId == -1) {
            return null;
        }

        UriComponentsBuilder builder;
        if (urlTMDBTrailer.contains("https://api.themoviedb.org/3/tv/")) {
            builder = UriComponentsBuilder.fromUriString(urlTMDBTrailer + movieId + "/season/" + "1" + "/videos")
                    .queryParam("api_key", TMDBApiKey);
        } else {
            builder = UriComponentsBuilder.fromUriString(urlTMDBTrailer + movieId + "/videos")
                    .queryParam("api_key", TMDBApiKey);
        }

        String responseBody = executeHttpGetRequest(builder.toUriString(), String.class);
        return responseBody != null ? extractTrailerKey(responseBody) : null;
    }

    private String extractTrailerKey(String responseBody) {
        return parseJson(responseBody, root -> {
            JSONArray results = root.getJSONArray("results");
            Predicate<JSONObject> isTrailerOfficial = result ->
                    "Trailer".equals(result.optString("type"));
            return IntStream.range(0, results.length())
                    .mapToObj(results::getJSONObject)
                    .filter(isTrailerOfficial)
                    .map(result -> result.optString("key"))
                    .findFirst()
                    .orElse(null);
        });
    }

    private Integer getMovieIdFromTMDB(String imdbId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(TMDBUrlImdb + imdbId)
                .queryParam("external_source", "imdb_id")
                .queryParam("api_key", TMDBApiKey);

        String responseBody = executeHttpGetRequest(builder.toUriString(), String.class);
        return responseBody != null ? parseMovieId(responseBody) : -1;
    }

    private Integer parseMovieId(String responseBody) {
        return parseJson(responseBody, root -> {
            JSONArray movieResults = root.optJSONArray("movie_results");
            JSONArray tvResults = root.optJSONArray("tv_results");
            movieResults = (movieResults == null || movieResults.length() == 0) ? tvResults : movieResults;
            if (movieResults != null && movieResults.length() > 0) {
                JSONObject firstMovie = movieResults.getJSONObject(0);
                return firstMovie.optInt("id", -1);
            } else {
                log.error("No movie results found in JSON response");
                return -1;
            }
        });
    }

    public List<String> getImdbIdsFromTMDB(Integer id) {
        List<String> allTMDBIds = new ArrayList<>();
        List<String> imdbIds = new ArrayList<>();

        for (int page = 1; page < 3; page++) {
            try {
                UriComponentsBuilder builder;
                if (id == 1) {
                    builder = UriComponentsBuilder.fromUriString(TMDBUrlTopRated)
                            .queryParam("language", "en-US")
                            .queryParam("page", page)
                            .queryParam("api_key", TMDBApiKey);
                } else {
                    builder = UriComponentsBuilder.fromUriString(TMDBUrlPopular)
                            .queryParam("language", "en-US")
                            .queryParam("page", page)
                            .queryParam("api_key", TMDBApiKey);
                }

                String responseBody = executeHttpGetRequest(builder.toUriString(), String.class);
                if (responseBody != null) {
                    List<String> TMDBIds = extractTMDBIdsFromTMDBList(responseBody);
                    allTMDBIds.addAll(TMDBIds);
                    for (String TMDBId : allTMDBIds) {
                        imdbIds.add(extractImdbIdFromTMDB(TMDBId));
                    }
                    log.info("Page {} IMDb IDs: {}", page, TMDBIds);
                }
            } catch (RestClientException e) {
                log.error("Error fetching data from TMDB: {}", e.getMessage());
            }
        }
        return imdbIds;
    }

    private List<String> extractTMDBIdsFromTMDBList(String responseBody) {
        return parseJson(responseBody, root -> {
            JSONArray results = root.getJSONArray("results");
            return IntStream.range(0, results.length())
                    .mapToObj(results::getJSONObject)
                    .map(result -> result.optString("id"))
                    .collect(Collectors.toList());
        });
    }

    public String extractImdbIdFromTMDB(String TMDBId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(TMDBUrlExternalLinks + TMDBId + "/external_ids")
                .queryParam("api_key", TMDBApiKey);

        String responseBody = executeHttpGetRequest(builder.toUriString(), String.class);
        return responseBody != null ? parseJson(responseBody, root -> root.getString("imdb_id")) : null;
    }

    private <T> T executeHttpGetRequest(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("HTTP request failed: {} {}", response.getStatusCodeValue(), response.getBody());
            return null;
        }
    }

    private <T> T parseJson(String json, Function<JSONObject, T> parser) {
        try {
            JSONObject root = new JSONObject(json);
            return parser.apply(root);
        } catch (JSONException e) {
            log.error("Error parsing JSON: {}", e.getMessage());
            return null;
        }
    }
}