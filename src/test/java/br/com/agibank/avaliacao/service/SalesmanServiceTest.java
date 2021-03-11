package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Salesman;
import br.com.agibank.avaliacao.repository.SalesmanRepository;
import br.com.agibank.avaliacao.stub.SalesmanStub;
import com.mongodb.MongoException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@QuarkusTest
public class SalesmanServiceTest {

    private SalesmanService salesmanService;

    @InjectMock
    private SalesmanRepository salesmanRepository;

    @BeforeEach
    public void setup() {
        salesmanService = new SalesmanService(salesmanRepository);
    }

    @Test
    public void givenRepositoryRetrieveSalesmanList_success_shouldReturnList() {
        when(salesmanService.getSalesByFileIdentifier(anyString())).thenReturn(List.of(SalesmanStub.get()));

        List<Salesman> result = salesmanService.getSalesByFileIdentifier("");

        assertThat(result.get(0), samePropertyValuesAs(SalesmanStub.get()));
    }

    @Test
    public void givenRepositoryRetrieveSalesmanList_error_shouldThrowException() {
        given(salesmanService.getSalesByFileIdentifier(anyString())).willThrow(new MongoException("Error while fetching data"));

        Assertions.assertThrows(MongoException.class, () -> {
            salesmanService.getSalesByFileIdentifier("");
        });

    }

    @Test
    public void givenSaveCustomer_repositoryShouldSaveSameEntity() {
        salesmanService.saveSalesMan(SalesmanStub.get());

        ArgumentCaptor<Salesman> customerArgumentCaptor = ArgumentCaptor.forClass(Salesman.class);

        then(salesmanRepository).should().persist(customerArgumentCaptor.capture());

        assertThat(customerArgumentCaptor.getValue(), samePropertyValuesAs(SalesmanStub.get()));
    }

}
