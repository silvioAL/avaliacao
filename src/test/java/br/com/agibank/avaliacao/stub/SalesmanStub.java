package br.com.agibank.avaliacao.stub;

import br.com.agibank.avaliacao.model.Salesman;

import java.math.BigDecimal;

public class SalesmanStub {

    public static Salesman get() {
        return Salesman.builder()
                .cpf("12312323423")
                .fileIdentifier("3")
                .layoutId("1")
                .salary(new BigDecimal("2323.11"))
                .name("name")
                .build();
    }
}
