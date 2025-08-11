package com.prueba_tecnica.prueba_tecnica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_tecnica.prueba_tecnica.model.Recharge;
import com.prueba_tecnica.prueba_tecnica.model.Supplier;
import com.prueba_tecnica.prueba_tecnica.model.Token;
import com.prueba_tecnica.prueba_tecnica.model.TransactionResult;
import com.prueba_tecnica.prueba_tecnica.service.RechargeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping("")
@CrossOrigin(origins = "*")
public class TransactionsController {

    @Autowired
    private RechargeService rechargeService;

    @GetMapping("/getAuthToken")
    public ResponseEntity<Object> getAuthToken() {
        Token token = rechargeService.getAuthToken();
        if (token == null) {
            return ResponseEntity.internalServerError().body("Se produjo un error al obtener el token.");
        }
        return ResponseEntity.ok(token);
    }
    
    @GetMapping("/suppliers")
    public ResponseEntity<Object> getSuppliers() {
        List<Supplier> suppliers = rechargeService.getAllSuppliers();
        if (suppliers == null || suppliers.isEmpty()) {
            return ResponseEntity.internalServerError().body("Se produjo un error al obtener los proveedores.");
        }
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/recharge")
    public ResponseEntity<Object> recharge(@RequestBody Recharge recharge) {
        TransactionResult result = rechargeService.processRecharge(recharge);
        
        if (result == null){
            return ResponseEntity.internalServerError().body("Se produjo un error al procesar la recarga.");
        }

        if (result.getTransactionalID().isEmpty()) {
            return ResponseEntity.internalServerError().body(result.getMessage());
        }

        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Object> getTransactions() {
        List<TransactionResult> transactions = rechargeService.getTransactionHistory();
        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }


}
