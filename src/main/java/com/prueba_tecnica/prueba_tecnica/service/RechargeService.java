package com.prueba_tecnica.prueba_tecnica.service;

import java.util.List;

import com.prueba_tecnica.prueba_tecnica.model.*;


public interface RechargeService {
    Token getAuthToken();
    List <Supplier> getAllSuppliers();
    TransactionResult processRecharge(Recharge recharge);
    List<TransactionResult> getTransactionHistory();
}
