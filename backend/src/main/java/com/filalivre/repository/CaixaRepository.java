package com.filalivre.repository;

import com.filalivre.model.Caixa;
import com.filalivre.model.StatusCaixa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    List<Caixa> findAllByAtivoTrueOrderByNumeroAsc();

    List<Caixa> findAllByMercadoId(Long mercadoId);

    long countByStatus(StatusCaixa status);

    List<Caixa> findAllByMercadoIdAndAtivoTrueOrderByNumeroAsc(Long mercadoId);

    Optional<Caixa> findByNumero(Integer numero);

    boolean existsByNumero(Integer numero);

    Optional<Caixa> findTopByOrderByNumeroDesc();

    Optional<Caixa> findTopByMercadoIdOrderByNumeroDesc(Long mercadoId);

    Optional<Caixa> findByIdAndAtivoTrue(Long id);
}
