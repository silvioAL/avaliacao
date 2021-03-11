package br.com.agibank.avaliacao.repository;

import br.com.agibank.avaliacao.model.Salesman;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class SalesmanRepository implements PanacheMongoRepository<Salesman> {

    public List<Salesman> findByFileIdentifier(final String fileIdentifier) {
        return list("fileIdentifier", fileIdentifier);
    }

}
