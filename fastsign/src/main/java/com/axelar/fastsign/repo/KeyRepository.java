package com.axelar.fastsign.repo;

import com.axelar.fastsign.data.KeyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<KeyRecord, Long> {
}
