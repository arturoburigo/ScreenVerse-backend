package com.screenverse.backend.service.watchmode;

import com.screenverse.backend.dto.watchmode.SearchResponseDTO;
import com.screenverse.backend.dto.watchmode.SearchResultDTO;
import com.screenverse.backend.dto.watchmode.TitleDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service for interacting with the WatchMode API
 * This service provides methods for searching titles and retrieving detailed information about specific titles
 */
@Service
public class WatchModeService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public WatchModeService(
            @Value("${watchmode.api.key}") String apiKey,
            @Value("${watchmode.api.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public SearchResponseDTO searchTitles(String searchValue, String searchField) {
        try {
            String url = UriComponentsBuilder.fromUriString(baseUrl + "/search")
                    .queryParam("apiKey", apiKey)
                    .queryParam("search_field", searchField)
                    .queryParam("search_value", searchValue)
                    .build()
                    .toUriString();

            SearchResponseDTO response = restTemplate.getForObject(url, SearchResponseDTO.class);

            // Fetch title details for the first 5 title results
            if (response != null && response.getTitle_results() != null && !response.getTitle_results().isEmpty()) {
                int limit = Math.min(5, response.getTitle_results().size());

                for (int i = 0; i < limit; i++) {
                    SearchResultDTO result = response.getTitle_results().get(i);
                    if (result.getId() != null) {
                        try {
                            TitleDetailsDTO details = getTitleDetails(result.getId());
                            result.setDetails(details);
                        } catch (Exception e) {
                            // Log the error but continue processing other results
                            System.err.println("Error fetching details for title ID " + result.getId() + ": " + e.getMessage());
                        }
                    }
                }
            }

            return response;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No results found for the search criteria", e);
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key", e);
            } else {
                throw new ResponseStatusException(e.getStatusCode(), "Error from WatchMode API: " + e.getMessage(), e);
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error connecting to WatchMode API", e);
        }
    }

    public TitleDetailsDTO getTitleDetails(Integer titleId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/title/" + titleId + "/details?region=BR&language=pt&append_to_response=sources")
                    .queryParam("apiKey", apiKey);
            String url = builder.build().toUriString();

            return restTemplate.getForObject(url, TitleDetailsDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Title with ID " + titleId + " not found", e);
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key", e);
            } else {
                throw new ResponseStatusException(e.getStatusCode(), "Error from WatchMode API: " + e.getMessage(), e);
            }
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error connecting to WatchMode API", e);
        }
    }
}
