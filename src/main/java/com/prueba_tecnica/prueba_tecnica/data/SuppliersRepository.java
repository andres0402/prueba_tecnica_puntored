package com.prueba_tecnica.prueba_tecnica.data;

import java.util.List;
import com.prueba_tecnica.prueba_tecnica.model.Supplier;

public interface SuppliersRepository {
    List<Supplier> findAll();
}
