package br.com.agibank.avaliacao.stub;

import br.com.agibank.avaliacao.model.Customer;

public class CustomerStub {

    public static Customer get() {
        return Customer.builder()
                .fileIdentifier("123")
                .businessArea("5")
                .layoutId("7")
                .name("name")
                .layoutId("111")
                .cnpj("777777")
                .build();
    }

}
