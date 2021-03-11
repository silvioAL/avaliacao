package br.com.agibank.avaliacao.model;

import io.quarkus.mongodb.panache.MongoEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@MongoEntity(collection = "Sale")
public class Sale {

    private String fileIdentifier;
    private String layoutId;
    private String saleId;
    private List<Long> itemId;
    private List<Double> itemQuantity;
    private List<BigDecimal> itemPrice;
    private String salesmanName;
    private BigDecimal totalAmount;

}
