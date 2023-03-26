package com.axelar.fastsign.helper;

import com.axelar.fastsign.data.DataRecord;
import com.axelar.fastsign.data.KeyRecord;
import com.axelar.fastsign.service.SigningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Signer {
    private static final Logger logger = LoggerFactory.getLogger(Signer.class);
    public static String sign(final String plainText, final KeyRecord keyRecord) throws Exception {
        byte[] encodedPrivK = Base64.getDecoder().decode(keyRecord.getPrivateKey());

        KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(encodedPrivK));

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public static List<DataRecord> signBatch(List<DataRecord> dataRecords, KeyRecord keyRecord) {
        dataRecords.parallelStream().forEach(dataRecord -> {
            try {
                dataRecord.setSignature(sign(dataRecord.getData(), keyRecord));
                dataRecord.setPublicKey(keyRecord.getPublicKey());
            }
            catch(Exception exception) {
                logger.error("Exception during signBatch", exception);
            }
        });

        return dataRecords;
    }
}
