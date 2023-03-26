package com.axelar.fastsign.data;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="allkeys")
@Getter
public class KeyRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 100000)
    private String privateKey;

    @Column(length = 100000)
    private String publicKey;

    public KeyRecord(){
    }

    public KeyRecord(Long id, String publicKey, String privateKey) {
        this.id = id;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
}
