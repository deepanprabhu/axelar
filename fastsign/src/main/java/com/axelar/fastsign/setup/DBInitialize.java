package com.axelar.fastsign.setup;

import com.axelar.fastsign.data.DataRecord;
import com.axelar.fastsign.data.KeyRecord;
import com.axelar.fastsign.repo.DataRepository;
import com.axelar.fastsign.repo.KeyRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.Base64;

@Configuration
@Log
public class DBInitialize {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private DataRepository dataRepository;


    @PostConstruct
    public void initialize(){
        try {
            Connection connection = dataSource.getConnection();

            /**
             * Create 100 private/public key pairs and store in keys.db
             * In production - we would use a key management service like AWS KMS and would not
             * store private/public keys in a database like this.
             */
            if(keyRepository.count() < 100) {
                keyRepository.deleteAllInBatch();

                KeyRecord keyRecord;
                for(int i=1;i<=100;i++){
                    log.info("Creating keyrecord");
                    KeyPair keyPair = generateKeyPair();
                    keyRecord = new KeyRecord((long)i,
                        Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                        Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
                    );
                    keyRepository.save(keyRecord);
                }
            }

            if(dataRepository.count() < 10000) {
                dataRepository.deleteAll();

                DataRecord dataRecord;
                for(int i=1;i<=10000;i++){
                    dataRecord = new DataRecord((long)i, String.format("this is data of line %s", i));
                    dataRepository.save(dataRecord);
                }
            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }
}
