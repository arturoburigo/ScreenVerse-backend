package com.screenverse.backend.service.watchmode;

import com.screenverse.backend.dto.watchmode.SearchResponseDTO;
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

            return restTemplate.getForObject(url, SearchResponseDTO.class);
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

    public TitleDetailsDTO getTitleDetails(Integer titleId, String region, String language, String appendToResponse) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/title/" + titleId + "/details/")
                    .queryParam("apiKey", apiKey);

            if (region != null) {
                builder.queryParam("regions", region);
            }

            if (language != null) {
                builder.queryParam("language", language);
            }

            if (appendToResponse != null) {
                builder.queryParam("append_to_response", appendToResponse);
            }

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
