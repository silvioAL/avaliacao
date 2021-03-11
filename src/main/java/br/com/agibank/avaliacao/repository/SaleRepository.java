package br.com.agibank.avaliacao.repository;

import br.com.agibank.avaliacao.model.Sale;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
public class SaleRepository implements PanacheMongoRepository<Sale> {

    public List<Sale> findByFileIdentifier(final String fileIdentifier) {
        return list("fileIdentifier", fileIdentifier);
    }

    public Map<String, BigDecimal> findAmountPerSalesMan() {
        return listAll().stream().collect(
                Collectors.groupingBy(
                        Sale::getSalesmanName,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getTotalAmount, BigDecimal::add)
                )
        );
    }
}
