package com.axelar.fastsign.service;

import com.axelar.fastsign.data.DataRecord;
import com.axelar.fastsign.data.KeyRecord;
import com.axelar.fastsign.helper.Signer;
import com.axelar.fastsign.repo.KeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class SigningService {

    private static final Logger logger = LoggerFactory.getLogger(SigningService.class);
    @Autowired
    KeyRepository keyRepository;

    @Autowired
    RecordService recordService;

    Map<Long, KeyRecord> keyMap = null;

    ConcurrentLinkedDeque<Long> keyQueue;

    protected final Object _lock = new Object();

    public void loadKeys() {
        keyQueue = new ConcurrentLinkedDeque<>();
        Map<Long, KeyRecord> map = new HashMap<>();
        for(KeyRecord keyRecord : keyRepository.findAll()){
            map.put(keyRecord.getId(), keyRecord);
            keyQueue.add(keyRecord.getId());
        }
        keyMap = Collections.unmodifiableMap(map);
    }

    public Long getAKeyToSign() {
        // LRU Implementation
        synchronized (_lock) {
            Long keyId = keyQueue.pollFirst();
            logger.info("Moving recently used key {} to end of queue",keyId);
            keyQueue.addLast(keyId);
            return keyId;
        }
    }
    public void signRecords(List<DataRecord> records) {
        try {
            Long keyId = getAKeyToSign();
            Signer.signBatch(records, keyMap.get(keyId));
        } catch(Exception exception) {
            logger.error("Error at signRecords", exception);
        }
    }
}
