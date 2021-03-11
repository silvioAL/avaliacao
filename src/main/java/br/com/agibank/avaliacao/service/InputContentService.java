package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Customer;
import br.com.agibank.avaliacao.model.PrefixType;
import br.com.agibank.avaliacao.model.Sale;
import br.com.agibank.avaliacao.model.Salesman;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InputContentService {

    private static final String SEPARATOR = "[,ç]";
    private final CustomerService customerService;
    private final SalesmanService salesmanService;
    private final SaleService saleService;

    public void saveLineContent(final String content, final String identifier) {

        final Pattern pattern = Pattern.compile(SEPARATOR);
        final String[] splittedContent = pattern.split(content);

        switch (PrefixType.fromValue(splittedContent[0])) {
            case SALE:
                final String idContent = splittedContent[2].replaceAll("[\\[\\]]", "");
                final Pattern itemPattern = Pattern.compile("-");
                final List<Long> ids = itemPattern.splitAsStream(idContent)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                final List<Double> itemQuantity = itemPattern.splitAsStream(splittedContent[3])
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());

                final String priceContent = splittedContent[4].replaceAll("[\\[\\]]", "");
                final List<BigDecimal> itemPrice = itemPattern.splitAsStream(priceContent)
                        .map(price -> new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN))
                        .collect(Collectors.toList());

                final BigDecimal totalAmount = itemPrice
                        .stream()
                        .map(s1 -> itemQuantity.stream().map(s2 -> s1.multiply(BigDecimal.valueOf(s2)))
                                .collect(Collectors.toList()))
                        .flatMap(Collection::stream)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_EVEN);

                saleService.saveSale(Sale.builder()
                        .fileIdentifier(identifier)
                        .itemId(ids)
                        .itemPrice(itemPrice)
                        .itemQuantity(itemQuantity)
                        .layoutId(splittedContent[0])
                        .salesmanName(splittedContent[5])
                        .saleId(splittedContent[1])
                        .totalAmount(totalAmount)
                        .build());
                break;

            case SALESMAN:
                salesmanService.saveSalesMan(Salesman.builder()
                        .fileIdentifier(identifier)
                        .cpf(splittedContent[1])
                        .layoutId(splittedContent[0])
                        .name(splittedContent[2])
                        .salary(new BigDecimal(splittedContent[3]))
                        .build());
                break;

            case CUSTOMER:
                customerService.saveCustomer(Customer.builder()
                        .fileIdentifier(identifier)
                        .businessArea(splittedContent[3])
                        .cnpj(splittedContent[1])
                        .layoutId(splittedContent[0])
                        .name(splittedContent[2])
                        .build());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + splittedContent[0]);
        }
    }
}
