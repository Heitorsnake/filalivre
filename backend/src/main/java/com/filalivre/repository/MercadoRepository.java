package com.filalivre.repository;

import com.filalivre.model.Mercado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoRepository extends JpaRepository<Mercado, Long> {
    Optional<Mercado> findByCodigoAcessoIgnoreCase(String codigoAcesso);
    Optional<Mercado> findByGestorId(Long gestorId);
}