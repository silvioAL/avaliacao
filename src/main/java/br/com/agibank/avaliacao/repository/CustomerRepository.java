package br.com.agibank.avaliacao.repository;

import br.com.agibank.avaliacao.model.Customer;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class CustomerRepository implements PanacheMongoRepository<Customer> {

    public List<Customer> findByFileIdentifier(final String fileIdentifier) {
        return list("fileIdentifier", fileIdentifier);
    }

}
