package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Sale;
import br.com.agibank.avaliacao.repository.SaleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SaleService {

    private final SaleRepository repository;

    public String getSmallerAmountSumSalesmanName() {
        final Map<String, BigDecimal> amountPerSalesMan = repository.findAmountPerSalesMan();

        return amountPerSalesMan
                .entrySet()
                .stream()
                .filter(saleData -> Objects.equals(saleData.getValue(), Collections.min(amountPerSalesMan.values())))
                .findFirst()
                .orElseThrow(NoSuchElementException::new).getKey();
    }

    public void saveSale(final Sale sale) {
        try {
            repository.persist(sale);
        } catch (Exception e) {
            log.error("Could not save sale: {}, error: {}", sale, e.getMessage());
        }
    }

    public List<Sale> getSalesByFileIdentifier(final String id) {
        return repository.findByFileIdentifier(id);
    }
}
