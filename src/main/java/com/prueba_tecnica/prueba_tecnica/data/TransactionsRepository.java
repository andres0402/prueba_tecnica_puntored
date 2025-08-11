package com.prueba_tecnica.prueba_tecnica.data;

import org.springframework.data.jpa.repository.JpaRepository;


import com.prueba_tecnica.prueba_tecnica.model.TransactionResult;

public interface TransactionsRepository extends JpaRepository<TransactionResult, String>{
}
