package com.assignment.controller;

import com.assignment.authentication.AuthRequest;
import com.assignment.authentication.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class AuthenticationController {
    private static final String AUTH_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    private static final String PROTECTED_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    // Store the bearer token after successful authentication
    private String bearerToken;
    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest authRequest) {
        // Convert the authRequest object to JSON string
        String requestBody = "{\"login_id\": \"" + authRequest.getLoginId() + "\", \"password\": \"" + authRequest.getPassword() + "\"}";

        // Create an HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(AUTH_API_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestBody));

            // Execute the request and get the response
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();

                // Check if the request was successful
                if (statusCode == 200) {
                    // Parse the response to extract the bearer token
                    String responseBody = EntityUtils.toString(response.getEntity());
                    AuthResponse authResponse = new ObjectMapper().readValue(responseBody, AuthResponse.class);

                    // Store the bearer token
                    bearerToken =   authResponse.getBearerToken();

                    return "Authentication successful!";
                } else {
                    return "Authentication failed!";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Authentication failed!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Authentication failed!";
        }
    }
    @GetMapping("/assignment_auth.jsp")
    public ResponseEntity<String> getProtectedResource() {
        // Check if the bearer token is available
        if (bearerToken == null || bearerToken.isEmpty()) {
            return new ResponseEntity<>("Authentication required!", HttpStatus.UNAUTHORIZED);
        }

        // Set the Authorization header with the bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        try {
            // Make the authenticated API call using RestTemplate
            ResponseEntity<String> response = restTemplate.getForEntity(PROTECTED_API_URL, String.class);

            // Handle the response and return the result
            if (response.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity<>("Protected resource: " + response.getBody(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error accessing protected resource!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (HttpClientErrorException.Unauthorized ex) {
            return new ResponseEntity<>("Invalid bearer token!", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error accessing protected resource!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}