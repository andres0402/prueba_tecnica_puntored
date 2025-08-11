package com.prueba_tecnica.prueba_tecnica.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.prueba_tecnica.prueba_tecnica.model.*;


@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${puntored.api.url}auth")
    private String authApiUrl;

    @Value("${puntored.api.url}getSuppliers")
    private String suppliersApiUrl;

    @Value("${puntored.api.url}buy")
    private String rechargeApiUrl;

    @Value("${puntored.api.key}")
    private String apiKey;

    @Value("${puntored.api.username}")
    private String apiUsername;

    @Value("${puntored.api.password}")
    private String apiPassword;

    @Override
    public Token getAuthToken() {
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-api-key", apiKey);

        // Body
        AuthRequestBody body = new AuthRequestBody(apiUsername, apiPassword); 

        // Entidad HTTP con headers y body
        HttpEntity<AuthRequestBody> request = new HttpEntity<>(body, headers);

        try{
            ResponseEntity<Token> response = restTemplate.exchange(
                authApiUrl,
                HttpMethod.POST,
                request,
                Token.class
            );

            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            // Manejo de errores
            return new Token(ex.getResponseBodyAsString());
        } catch (Exception ex) {
             return new Token(ex.getMessage());
        }
    }


    @Override
    public List<Supplier> getAllSuppliers() {
        Token token = getAuthToken();

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", token.getToken());

        // Entidad HTTP con headers
        HttpEntity<AuthRequestBody> request = new HttpEntity<>(headers);

        // Petición GET
        try{
            ResponseEntity<Supplier[]> response = restTemplate.exchange(
                suppliersApiUrl,
                HttpMethod.GET,
                request,
                Supplier[].class
            );

            return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
        } catch (HttpStatusCodeException ex) {
            // Manejo de errores
            return List.of();
        } catch (Exception ex) {
            return List.of();
        }
    }

    @Override
    public TransactionResult processRecharge(Recharge recharge) {
        Token token = getAuthToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("authorization", token.getToken());

        HttpEntity<Recharge> request = new HttpEntity<>(recharge, headers);

        try {
            ResponseEntity<TransactionResult> response = restTemplate.exchange(
                rechargeApiUrl,
                HttpMethod.POST,
                request,
                TransactionResult.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (HttpStatusCodeException ex) {
            return new TransactionResult(ex.getResponseBodyAsString(), "", "", "");
        } catch (Exception ex) {
            return new TransactionResult(ex.getMessage(), "", "", "");
        }
    }
}