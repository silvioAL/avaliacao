package br.com.agibank.avaliacao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public enum PrefixType {

    SALESMAN("001"), CUSTOMER("002"), SALE("003");

    private final String code;

    public static PrefixType fromValue(final String value) {
        return List.of(PrefixType.values()).stream()
                .filter(prefixType -> prefixType.getCode().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
