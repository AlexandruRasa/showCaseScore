package com.showcaseScore.movieApp.util;

import com.showcaseScore.movieApp.dtos.MovieDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class CallApi {

    public final String URL_OMDB = "https://www.omdbapi.com/";
    public final String URL_TMDB_IMDB = "https://api.themoviedb.org/3/find/";
    private final String URL_TMDB_LIST_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    private final String URL_TMDB_LIST_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private final String URL_TMDB_EXTERNAL_LINKS = "https://api.themoviedb.org/3/movie/";
    private final String API_KEY_TMDB = "API_KEY_TMDB";
    private final String API_KEY_OMDB = "API_KEY_OMDB";
    private final RestTemplate restTemplate;

    @Autowired
    public CallApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public MovieDTO getMovieFromOmdb(String imdbId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_OMDB)
                .queryParam("i", imdbId)
                .queryParam("apikey", API_KEY_OMDB)
                .queryParam("plot", "full");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<MovieDTO> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, MovieDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            MovieDTO movieDto = response.getBody();
            if (movieDto != null) {
                return movieDto;
            } else {
                log.error("getMovieFromOmdb ---> Error: No movie data found in response");
            }
        } else {
            log.error("getMovieFromOmdb ---> Error: {} {}", response.getStatusCodeValue(), response.getBody());
        }
        return null;
    }

    public String getTrailerFromTmdb(String imdbId, String urlTmdbTrailer) {
        Integer movieId = getMovieIdFromTmdb(imdbId);
        if (movieId == -1) {
            return null;
        }
        UriComponentsBuilder builder;
        log.info("ULR before if block --->" + urlTmdbTrailer);
        if (urlTmdbTrailer.contains("https://api.themoviedb.org/3/tv/")) {
            builder = UriComponentsBuilder.fromUriString(urlTmdbTrailer + movieId + "/season/" + "1" + "/videos")
                    .queryParam("api_key", API_KEY_TMDB);
            log.info("URL --->" + urlTmdbTrailer + movieId + "/season/" + "1" + "/videos");
        } else {
            builder = UriComponentsBuilder.fromUriString(urlTmdbTrailer + movieId + "/videos")
                    .queryParam("api_key", API_KEY_TMDB);
            log.info("URL --->" + urlTmdbTrailer + movieId + "/videos");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            String key = extractTrailerKey(responseBody);
            log.info("getTrailerFromTmdb ---> " + key);
            return key;
        } else {
            log.info("getTrailerFromTmdb ---> Error: " + response.getStatusCodeValue());
            return null;
        }
    }

    private String extractTrailerKey(String responseBody) {
        try {
            JSONObject root = new JSONObject(responseBody);
            JSONArray results = root.getJSONArray("results");

            Predicate<JSONObject> isTrailerOfficial = result ->
                    "Trailer".equals(result.optString("type"));

            return IntStream.range(0, results.length())
                    .mapToObj(results::getJSONObject)
                    .filter(isTrailerOfficial)
                    .map(result -> result.optString("key"))
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            log.error("extractTrailerKey ---> Error parsing JSON: " + e.getMessage());
            return null;
        }
    }

    private Integer getMovieIdFromTmdb(String imdbId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_TMDB_IMDB + imdbId)
                .queryParam("external_source", "imdb_id")
                .queryParam("api_key", API_KEY_TMDB);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            Integer movieId = parseMovieId(responseBody);
            log.info("getMovieIdFromTmdb ---> " + movieId);
            return movieId;
        } else {
            log.info("Error: " + response.getStatusCodeValue());
            return -1;
        }
    }

    private Integer parseMovieId(String responseBody) {
        log.info("parseMovieId ---> " + responseBody);
        try {
            JSONObject root = new JSONObject(responseBody);
            JSONArray movieResults = root.optJSONArray("movie_results");
            JSONArray tvResults = root.optJSONArray("tv_results");
            movieResults = (movieResults == null || movieResults.length() == 0) ? tvResults : movieResults;
            log.info("MOVIERESULTS: --->" + movieResults);
            if (movieResults != null && movieResults.length() > 0) {
                JSONObject firstMovie = movieResults.getJSONObject(0);
                return firstMovie.optInt("id", -1);
            } else {
                log.error("parseMovieId ---> No movie results found in JSON response");
                return -1;
            }
        } catch (JSONException e) {
            log.error("parseMovieId ---> Error parsing JSON: {}", e.getMessage());
            return -1;
        }
    }

    public List<String> getImdbIdsFromTmdb(Integer id) {
        List<String> allTmdbIds = new ArrayList<>();
        List<String> imdbIds = new ArrayList<>();

        for (int page = 1; page < 3; page++) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                UriComponentsBuilder builder;

                if (id == 1) {
                    builder = UriComponentsBuilder.fromUriString(URL_TMDB_LIST_TOP_RATED)
                            .queryParam("language", "en-US")
                            .queryParam("page", page)
                            .queryParam("api_key", API_KEY_TMDB);
                } else {
                    builder = UriComponentsBuilder.fromUriString(URL_TMDB_LIST_POPULAR)
                            .queryParam("language", "en-US")
                            .queryParam("page", page)
                            .queryParam("api_key", API_KEY_TMDB);
                }
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                log.info("getImdbIdsFromTmdb ---> " + response);

                if (response.getStatusCode() == HttpStatus.OK) {
                    String responseBody = response.getBody();
                    List<String> tmdbIds = extractTmdbIdsFromTmdbList(responseBody);
                    allTmdbIds.addAll(tmdbIds);
                    for (String i: allTmdbIds) {
                        imdbIds.add(extractImdbIdFromTmdb(i));
                    }
                    log.info("Page " + page + " IMDb IDs: " + tmdbIds);
                } else {
                    log.info("Error: " + response.getStatusCodeValue());
                }
            } catch (RestClientException e) {
                log.error("getImdbIdsFromTmdb ---> Error fetching data from TMDB: " + e.getMessage());
            }
        }
        return imdbIds;
    }

    private List<String> extractTmdbIdsFromTmdbList(String responseBody) {
        try {
            JSONObject root = new JSONObject(responseBody);
            JSONArray results = root.getJSONArray("results");

            return IntStream.range(0, results.length())
                    .mapToObj(results::getJSONObject)
                    .map(result -> result.optString("id"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("extractTmdbIdsFromTmdbList ---> Error parsing JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public String extractImdbIdFromTmdb(String tmdbId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL_TMDB_EXTERNAL_LINKS + tmdbId + "/external_ids")
                .queryParam("api_key", API_KEY_TMDB);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                String imdbId = jsonResponse.getString("imdb_id");
                log.info("extractImdbIdFromTmdb ---> " + imdbId);
                return imdbId;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("extractImdbIdFromTmdb ---> Error: " + response.getStatusCodeValue());
            return null;
        }
    }
}
