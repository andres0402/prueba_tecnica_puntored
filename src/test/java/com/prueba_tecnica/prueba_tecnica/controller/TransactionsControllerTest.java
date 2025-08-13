package com.prueba_tecnica.prueba_tecnica.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.prueba_tecnica.prueba_tecnica.model.Recharge;
import com.prueba_tecnica.prueba_tecnica.model.Supplier;
import com.prueba_tecnica.prueba_tecnica.model.Token;
import com.prueba_tecnica.prueba_tecnica.model.TransactionResult;
import com.prueba_tecnica.prueba_tecnica.service.RechargeService;

@WebMvcTest(TransactionsController.class)
class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RechargeService rechargeService;

    @Test
    @DisplayName("GET /getAuthToken - OK")
    void getAuthToken_ok() throws Exception {
        when(rechargeService.getAuthToken()).thenReturn(new Token("abc123"));

        mockMvc.perform(get("/getAuthToken"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("abc123"));
    }

    @Test
    @DisplayName("GET /getAuthToken - Error 500")
    void getAuthToken_error() throws Exception {
        when(rechargeService.getAuthToken()).thenReturn(null);

        mockMvc.perform(get("/getAuthToken"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Se produjo un error al obtener el token."));
    }

    @Test
    @DisplayName("GET /suppliers - OK")
    void getSuppliers_ok() throws Exception {
        when(rechargeService.getAllSuppliers()).thenReturn(List.of(new Supplier("1", "Proveedor A")));

        mockMvc.perform(get("/suppliers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].name").value("Proveedor A"));
    }

    @Test
    @DisplayName("GET /suppliers - Error 500 si lista vacía")
    void getSuppliers_empty_error() throws Exception {
        when(rechargeService.getAllSuppliers()).thenReturn(List.of());

        mockMvc.perform(get("/suppliers"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Se produjo un error al obtener los proveedores."));
    }

    @Test
    @DisplayName("POST /recharge - OK")
    void recharge_ok() throws Exception {
        TransactionResult result = new TransactionResult("tx-1", "Transacción exitosa", "3001234567", "10000");
        when(rechargeService.processRecharge(any(Recharge.class))).thenReturn(result);

        String body = "{\n" +
                "  \"cellPhone\": \"3001234567\",\n" +
                "  \"value\": 10000.0,\n" +
                "  \"supplierId\": \"1\"\n" +
                "}";

        mockMvc.perform(post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionalID").value("tx-1"))
            .andExpect(jsonPath("$.message").value("Transacción exitosa"))
            .andExpect(jsonPath("$.cellPhone").value("3001234567"))
            .andExpect(jsonPath("$.value").value("10000"));
    }

    @Test
    @DisplayName("POST /recharge - Error 500 cuando servicio devuelve null")
    void recharge_null_error() throws Exception {
        when(rechargeService.processRecharge(any(Recharge.class))).thenReturn(null);

        String body = "{\n" +
                "  \"cellPhone\": \"3000000000\",\n" +
                "  \"value\": 5000.0,\n" +
                "  \"supplierId\": \"1\"\n" +
                "}";

        mockMvc.perform(post("/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Se produjo un error al procesar la recarga."));
    }


    @Test
    @DisplayName("GET /transactions - OK")
    void getTransactions_ok() throws Exception {
        when(rechargeService.getTransactionHistory()).thenReturn(
                List.of(new TransactionResult("tx-1", "OK", "3001234567", "10000"))
        );

        mockMvc.perform(get("/transactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].transactionalID").value("tx-1"));
    }

    @Test
    @DisplayName("GET /transactions - 204 No Content cuando no hay registros")
    void getTransactions_noContent() throws Exception {
        when(rechargeService.getTransactionHistory()).thenReturn(List.of());

        mockMvc.perform(get("/transactions"))
            .andExpect(status().isNoContent());
    }
}


