package br.com.agibank.avaliacao.stub;

import br.com.agibank.avaliacao.model.Sale;

import java.math.BigDecimal;
import java.util.List;

public class SaleStub {

    public static Sale get() {

        return Sale.builder()
                .fileIdentifier("123")
                .itemId(List.of(5L))
                .saleId("72637")
                .itemPrice(List.of(new BigDecimal("30.21")))
                .itemQuantity(List.of(2.00))
                .salesmanName("name")
                .layoutId("3")
                .totalAmount(new BigDecimal("23.33"))
                .build();
    }
}
