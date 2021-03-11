package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Salesman;
import br.com.agibank.avaliacao.repository.SalesmanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SalesmanService {

    private final SalesmanRepository repository;

    public void saveSalesMan(final Salesman salesman) {
        try {
            repository.persist(salesman);
        } catch (Exception e) {
            log.error("Could not save salesman: {}, error: {}", salesman, e);
        }
    }

    public List<Salesman> getSalesByFileIdentifier(final String id) {
        return repository.findByFileIdentifier(id);
    }
}
