package com.ens.iam.adapter.outbound.infrastructure.persistence.repository;

import com.ens.iam.adapter.outbound.infrastructure.persistence.entity.ClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, String> {
    @Query(value = "SELECT * FROM CLIENT " +
            " WHERE  client_id = :clientId and enabled is true "
            , nativeQuery = true)
    Optional<ClientEntity> findByClientId(String clientId);

    boolean existsByRealmIdAndClientId(String realmId, String clientId);
}

