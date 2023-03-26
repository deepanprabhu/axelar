package com.axelar.fastsign.repo;

import com.axelar.fastsign.data.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<DataRecord, Long> {
}
