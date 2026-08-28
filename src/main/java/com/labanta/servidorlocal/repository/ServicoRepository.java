package com.labanta.servidorlocal.repository;

import com.labanta.servidorlocal.model.ServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoModel, Long> {
    // Encontra todos os serviços que estão ativos
    List<ServicoModel> findByestativoTrue();

    // Encontra serviços que custam menos do que um determinado valor
    List<ServicoModel> findByPrecoLessThan(Double valorMaximo);

    // Query Method automática para pesquisa case-insensitive
    List<ServicoModel> findByTituloContainingIgnoreCase(String titulo);


}
