package com.filalivre.repository;

import com.filalivre.model.Solicitacao;
import com.filalivre.model.StatusSolicitacao;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

        List<Solicitacao> findByCaixa_Mercado_IdAndStatusOrderByCriadoEmDesc(
            Long mercadoId, StatusSolicitacao status);

    List<Solicitacao> findByOperadorIdOrderByCriadoEmDesc(Long operadorId);

        List<Solicitacao> findTop100ByCaixa_Mercado_IdOrderByCriadoEmDesc(Long mercadoId);

        List<Solicitacao> findByCaixa_Mercado_IdAndStatusIn(
            Long mercadoId, Collection<StatusSolicitacao> statuses);

        long countByCaixa_Mercado_Id(Long mercadoId);

        long countByCaixa_Mercado_IdAndStatus(Long mercadoId, StatusSolicitacao status);

    boolean existsByCaixaIdAndStatus(Long caixaId, StatusSolicitacao status);
}
