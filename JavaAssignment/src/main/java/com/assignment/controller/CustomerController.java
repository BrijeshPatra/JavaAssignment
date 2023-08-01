package com.assignment.controller;

import com.assignment.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
public class CustomerController {
    private static final String CUSTOMER_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
    private static final String CUSTOMER_LIST_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
    private static final String CUSTOMER_DELETION_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
    private static final String CUSTOMER_UPDATE_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Customer customer
    ) {
        // Check if first_name and last_name are provided
        if (customer.getFirst_name() == null || customer.getLast_name() == null) {
            return new ResponseEntity<>("First Name or Last Name is missing", HttpStatus.BAD_REQUEST);
        }
        String requestBody = "{\"cmd\": \"create\"," +
                "\"first_name\": \"" + customer.getFirst_name() + "\"," +
                "\"last_name\": \"" + customer.getLast_name() + "\"," +
                "\"street\": \"" + customer.getStreet() + "\"," +
                "\"address\": \"" + customer.getAddress() + "\"," +
                "\"city\": \"" + customer.getCity() + "\"," +
                "\"state\": \"" + customer.getState() + "\"," +
                "\"email\": \"" + customer.getEmail() + "\"," +
                "\"phone\": \"" + customer.getPhone() + "\"}";

// Set the Authorization header with the bearer token received in the authentication API call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the authenticated API call using RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(CUSTOMER_API_URL, httpEntity, String.class);

            // Handle the response and return the result
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create the Customer", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to create the Customer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get_customer_list")
    public ResponseEntity<List<Customer>> getCustomerList(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        // Set the Authorization header with the bearer token received in the authentication API call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        try {
            // Make the authenticated API call using RestTemplate
            ResponseEntity<Customer[]> response = restTemplate.getForEntity(CUSTOMER_LIST_API_URL, Customer[].class);

            // Handle the response and return the result
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Customer> customerList = Arrays.asList(response.getBody());
                return new ResponseEntity<>(customerList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteCustomer(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("uuid") String uuid
    ) {
        // Check if UUID is provided
        if (uuid == null || uuid.isEmpty()) {
            return new ResponseEntity<>("UUID not found", HttpStatus.BAD_REQUEST);
        }

        // Create the request body
        String requestBody = "{\"cmd\": \"delete\"," +
                "\"uuid\": \"" + uuid + "\"}";

        // Set the Authorization header with the bearer token received in the authentication API call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the authenticated API call using RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(CUSTOMER_DELETION_API_URL, httpEntity, String.class);

            // Handle the response and return the result
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("Error Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/update-customer")
    public ResponseEntity<String> updateCustomer(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("uuid") String uuid,
            @RequestBody Customer customer
    ) {
        // Check if UUID is provided
        if (uuid == null || uuid.isEmpty()) {
            return new ResponseEntity<>("UUID not found", HttpStatus.BAD_REQUEST);
        }

        // Check if the request body is empty
        if (customer == null) {
            return new ResponseEntity<>("Body is Empty", HttpStatus.BAD_REQUEST);
        }

        // Create the request body
        String requestBody = "{\"cmd\": \"update\"," +
                "\"uuid\": \"" + uuid + "\"," +
                "\"first_name\": \"" + customer.getFirst_name() + "\"," +
                "\"last_name\": \"" + customer.getLast_name() + "\"," +
                "\"street\": \"" + customer.getStreet() + "\"," +
                "\"address\": \"" + customer.getAddress() + "\"," +
                "\"city\": \"" + customer.getCity() + "\"," +
                "\"state\": \"" + customer.getState() + "\"," +
                "\"email\": \"" + customer.getEmail() + "\"," +
                "\"phone\": \"" + customer.getPhone() + "\"}";

        // Set the Authorization header with the bearer token received in the authentication API call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the authenticated API call using RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(CUSTOMER_UPDATE_API_URL, httpEntity, String.class);

            // Handle the response and return the result
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ResponseEntity<>("UUID not found", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>("Invalid Command", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("Invalid Authorization", HttpStatus.UNAUTHORIZED);
        }
    }
}
