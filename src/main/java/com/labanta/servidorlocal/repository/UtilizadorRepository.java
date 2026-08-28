package com.labanta.servidorlocal.repository;

import com.labanta.servidorlocal.model.UtilizadorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilizadorRepository extends JpaRepository<UtilizadorModel, Long> {
    Optional<UtilizadorModel> findByUsername(String username);
}
