package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Customer;
import br.com.agibank.avaliacao.repository.CustomerRepository;
import br.com.agibank.avaliacao.stub.CustomerStub;
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
public class CustomerServiceTest {

    private CustomerService customerService;

    @InjectMock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void givenRepositoryRetrieveCustomerList_success_shouldReturnList() {
        when(customerService.getCustomersByFileIdentifier(anyString())).thenReturn(List.of(CustomerStub.get()));

        List<Customer> result = customerService.getCustomersByFileIdentifier("");

        assertThat(result.get(0), samePropertyValuesAs(CustomerStub.get()));
    }

    @Test
    public void givenRepositoryRetrieveCustomerList_error_shouldThrowException() {
        given(customerService.getCustomersByFileIdentifier(anyString())).willThrow(new MongoException("Error while fetching data"));

        Assertions.assertThrows(MongoException.class, () -> {
            customerService.getCustomersByFileIdentifier("");
        });

    }

    @Test
    public void givenSaveCustomer_repositoryShouldSaveSameEntity() {
        customerService.saveCustomer(CustomerStub.get());

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        then(customerRepository).should().persist(customerArgumentCaptor.capture());

        assertThat(customerArgumentCaptor.getValue(), samePropertyValuesAs(CustomerStub.get()));
    }

}