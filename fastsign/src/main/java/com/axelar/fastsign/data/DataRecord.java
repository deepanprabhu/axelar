package com.axelar.fastsign.data;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name="records")
@Data
public class DataRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 100000)
    private String data;

    @Column(length = 100000)
    @Nullable
    private String signature;

    @Column(length = 100000)
    @Nullable
    private String publicKey;

    public DataRecord(){

    }
    public DataRecord(Long id, String data) {
        this.id = id;
        this.data = data;
        this.signature = null;
        this.publicKey = null;
    }
}
