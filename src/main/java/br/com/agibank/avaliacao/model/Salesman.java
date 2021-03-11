package br.com.agibank.avaliacao.model;

import io.quarkus.mongodb.panache.MongoEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "Salesman")
public class Salesman {

    private String fileIdentifier;
    private String layoutId;
    private String cpf;
    private String name;
    private BigDecimal salary;

}
