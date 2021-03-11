package br.com.agibank.avaliacao.service;


import br.com.agibank.avaliacao.model.Sale;
import br.com.agibank.avaliacao.repository.SaleRepository;
import br.com.agibank.avaliacao.stub.SaleStub;
import com.mongodb.MongoException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@QuarkusTest
public class SaleServiceTest {

    private SaleService saleService;

    @InjectMock
    private SaleRepository saleRepository;

    @BeforeEach
    public void setup() {
        saleService = new SaleService(saleRepository);
    }

    @Test
    public void givenRepositoryRetrieveSalesList_success_shouldReturnList() {
        when(saleService.getSalesByFileIdentifier(anyString())).thenReturn(List.of(SaleStub.get()));

        List<Sale> result = saleService.getSalesByFileIdentifier("");

        assertThat(result.get(0), samePropertyValuesAs(SaleStub.get()));
    }

    @Test
    public void givenRepositoryRetrieveSalesList_error_shouldThrowException() {
        given(saleService.getSalesByFileIdentifier(anyString())).willThrow(new MongoException("Error while fetching data"));

        Assertions.assertThrows(MongoException.class, () -> {
            saleService.getSalesByFileIdentifier("");
        });

    }

    @Test
    public void givenSaveCustomer_repositoryShouldSaveSameEntity() {
        saleService.saveSale(SaleStub.get());

        ArgumentCaptor<Sale> saleArgumentCaptor = ArgumentCaptor.forClass(Sale.class);

        then(saleRepository).should().persist(saleArgumentCaptor.capture());

        assertThat(saleArgumentCaptor.getValue(), samePropertyValuesAs(SaleStub.get()));
    }

    @Test
    public void givenRepositoryGetSalesAmountPerSalesman_success_shouldReturnName() {
        given(saleRepository.findAmountPerSalesMan()).willReturn(Map.of("WORSE SALESMAN", new BigDecimal("1.99"),
                "BEST SALESMAN", new BigDecimal("23.44"),
                "ANY SALESMAN", new BigDecimal("10.00")));

        final String result = saleService.getSmallerAmountSumSalesmanName();

        assertThat(result, is("WORSE SALESMAN"));

    }

}
