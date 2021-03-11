package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Customer;
import br.com.agibank.avaliacao.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public List<Customer> getCustomersByFileIdentifier(final String fileIdentifier) {
        return repository.findByFileIdentifier(fileIdentifier);
    }

    public void saveCustomer(final Customer customer) {
        try {
            repository.persist(customer);
        } catch (Exception e) {
            log.error("Could not save customer: {}, error: {}", customer, e);
        }
    }
}
