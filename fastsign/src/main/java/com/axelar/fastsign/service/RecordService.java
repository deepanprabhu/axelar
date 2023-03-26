package com.axelar.fastsign.service;

import com.axelar.fastsign.FastsignApplication;
import com.axelar.fastsign.data.DataRecord;
import com.axelar.fastsign.repo.DataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    private static final Logger logger = LoggerFactory.getLogger(RecordService.class);
    @Autowired
    DataRepository dataRepository;
    
    private int batchSize;

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getTotalBatch() {
        Page<DataRecord> page =  dataRepository.findAll(PageRequest.of(1, batchSize));
        return page.getTotalPages();
    }

    public List<DataRecord> readBatch(int ordinal){
        Page<DataRecord> page =  dataRepository.findAll(PageRequest.of(ordinal, batchSize));
        return page.getContent();
    }

    public void writeBatch(List<DataRecord> records){
        try {
            dataRepository.saveAll(records);
        }
        catch(Exception exception) {
            logger.error("Exception in writeBatch");
        }
    }
}
