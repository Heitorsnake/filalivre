package com.filalivre.repository;

import com.filalivre.model.Solicitacao;
import com.filalivre.model.StatusSolicitacao;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

        List<Solicitacao> findByCaixa_Mercado_IdAndStatusOrderByCriadoEmDesc(
            Long mercadoId, StatusSolicitacao status);

    List<Solicitacao> findByOperadorIdOrderByCriadoEmDesc(Long operadorId);

        List<Solicitacao> findTop100ByCaixa_Mercado_IdOrderByCriadoEmDesc(Long mercadoId);

        List<Solicitacao> findByCaixa_Mercado_IdAndStatusIn(
            Long mercadoId, Collection<StatusSolicitacao> statuses);

        long countByCaixa_Mercado_Id(Long mercadoId);

        long countByCaixa_Mercado_IdAndStatus(Long mercadoId, StatusSolicitacao status);

        long countByStatus(StatusSolicitacao status);

        List<Solicitacao> findByStatusIn(Collection<StatusSolicitacao> statuses);

    boolean existsByCaixaIdAndStatus(Long caixaId, StatusSolicitacao status);

    @EntityGraph(attributePaths = {"caixa", "operador"})
    @Query("""
        SELECT s FROM Solicitacao s
        WHERE (:mercadoId IS NULL OR s.caixa.mercado.id = :mercadoId)
          AND s.criadoEm >= :inicio
          AND s.criadoEm < :fim
          AND (:status IS NULL OR s.status = :status)
        ORDER BY s.criadoEm DESC
        """)
    List<Solicitacao> buscarParaRelatorio(
            @Param("mercadoId") Long mercadoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") StatusSolicitacao status);
}
