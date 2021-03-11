package br.com.agibank.avaliacao.model;

import io.quarkus.mongodb.panache.MongoEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@MongoEntity(collection = "Customer")
public class Customer {

    private String fileIdentifier;
    private String layoutId;
    private String cnpj;
    private String name;
    private String businessArea;

}
