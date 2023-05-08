package org.logistics.entityService.service.impl;

import com.google.maps.GeoApiContext;
import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.data.GetGeocodeDetailsModel;
import org.logistics.entityService.model.data.GetPlaceDetailsModel;
import org.logistics.entityService.model.data.RouteDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class MapService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Value("${google.places.api.key}")
    private String googlePlacesApiKey;

    @Value("${google.routes.api.key}")
    private String googleRoutesApiKey;

    @Value("${google.maps.url}")
    private String mapsUrl;

    private RestTemplate restTemplate;

    public GeoApiContext context = null;

    public MapService() {
        restTemplate = new RestTemplate();
    }

    public void setGeoApiContext() {
        if(context == null) {
            log.info("setting geo api context...");
            this.context = new GeoApiContext.Builder()
                    .apiKey(googleMapsApiKey)
                    .build();
        }
    }

    public GetGeocodeDetailsModel getPlaceIdBasedOnLatLong(Float lat, Float lng) {
        log.info("Request to fetch place id for location({}, {}) received", lat, lng);
        String latlng = lat + "," + lng;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mapsUrl)
                .path("/maps/api/geocode/json")
                .queryParam("latlng", latlng)
                .queryParam("key", googleMapsApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GetGeocodeDetailsModel> response = null;
        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    GetGeocodeDetailsModel.class);
        } catch (Exception ex) {
            throw new EntityServiceException(ex.getLocalizedMessage());
        }
        if(response.getStatusCode() != HttpStatus.OK)
            throw new EntityServiceException("Bad data used to make the call.");
        return response.getBody();
    }

    public GetPlaceDetailsModel getPlaceDetailsBasedOnId(String placeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mapsUrl)
                .path("/maps/api/place/details/json")
                .queryParam("place_id", placeId)
                .queryParam("key", googlePlacesApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<GetPlaceDetailsModel> response = null;
        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    GetPlaceDetailsModel.class);
        } catch (Exception ex) {
            throw new EntityServiceException(ex.getLocalizedMessage());
        }
        if(response.getStatusCode() != HttpStatus.OK || ((GetPlaceDetailsModel)response.getBody()).getResult() == null)
            throw new EntityServiceException("Bad data used to make the call.");
        return response.getBody();
    }

    public RouteDetails getRouteDetails(String origin, String destination) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mapsUrl)
                .path("/maps/api/distancematrix/json")
                .queryParam("origins", origin)
                .queryParam("destinations", destination)
                .queryParam("units", "metric")
                .queryParam("mode", "driving")
                .queryParam("key", googleRoutesApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<RouteDetails> response = null;
        try {
            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    RouteDetails.class);
        } catch (Exception ex) {
            throw new EntityServiceException(ex.getLocalizedMessage());
        }
        log.info("Route Response : {}", response.getBody());
        if(response.getStatusCode() != HttpStatus.OK || ((RouteDetails)response.getBody()).getRows() == null)
            throw new EntityServiceException("Bad data used to make the call.");
        if(!response.getBody().getRows().get(0).getElements().get(0).getStatus().equals("OK"))
            throw new EntityServiceException("No possible driving routes present for the following locations.");
        return response.getBody();
    }
}
