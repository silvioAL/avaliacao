package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Customer;
import br.com.agibank.avaliacao.model.Sale;
import br.com.agibank.avaliacao.model.Salesman;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@QuarkusTest
class InputContentServiceTest {

    private InputContentService service;

    @InjectMock
    private CustomerService customerService;

    @InjectMock
    private SalesmanService salesmanService;

    @InjectMock
    private SaleService saleService;

    @BeforeEach
    public void setup() {
        service = new InputContentService(customerService, salesmanService, saleService);
    }

    @Test
    public void givenCustomerLineContent_shouldSaveCustomer() {
        service.saveLineContent("002ç2345675434544345çJose da SilvaçRural", "REPORT.dat");

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        then(customerService).should().saveCustomer(customerArgumentCaptor.capture());

        final Customer result = customerArgumentCaptor.getValue();

        assertThat(result.getBusinessArea(), is("Rural"));
        assertThat(result.getCnpj(), is("2345675434544345"));
        assertThat(result.getFileIdentifier(), is("REPORT.dat"));
        assertThat(result.getLayoutId(), is("002"));
        assertThat(result.getName(), is("Jose da Silva"));

        then(salesmanService).should(never()).saveSalesMan(any());
        then(saleService).should(never()).saveSale(any());

    }

    @Test
    public void givenSaleLineContent_shouldSaveSale() {
        service.saveLineContent("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro", "REPORT.dat");

        ArgumentCaptor<Sale> saleArgumentCaptor = ArgumentCaptor.forClass(Sale.class);

        then(saleService).should().saveSale(saleArgumentCaptor.capture());

        final Sale result = saleArgumentCaptor.getValue();

        assertThat(result.getFileIdentifier(), is("REPORT.dat"));
        assertThat(result.getLayoutId(), is("003"));
        assertThat(result.getItemId(), containsInAnyOrder(Arrays.asList(1L, 10L, 100L).toArray()));
        assertThat(result.getItemQuantity(), containsInAnyOrder(Arrays.asList(2.0, 30.0, 2.5).toArray()));
        assertThat(result.getItemPrice(), containsInAnyOrder(Arrays.asList(new BigDecimal("3.00"), new BigDecimal("40.00"), new BigDecimal("3.10")).toArray()));
        assertThat(result.getSaleId(), is("10"));
        assertThat(result.getSalesmanName(), is("Pedro"));
        assertThat(result.getTotalAmount(), is(new BigDecimal("1590.45")));

        then(salesmanService).should(never()).saveSalesMan(any());
        then(customerService).should(never()).saveCustomer(any());

    }

    @Test
    public void givenSalesmanLineContent_shouldSaveSalesman() {
        service.saveLineContent("001ç3245678865434çPauloç40000.99", "REPORT.dat");

        ArgumentCaptor<Salesman> salesmanArgumentCaptor = ArgumentCaptor.forClass(Salesman.class);

        then(salesmanService).should().saveSalesMan(salesmanArgumentCaptor.capture());

        final Salesman result = salesmanArgumentCaptor.getValue();

        assertThat(result.getCpf(), is("3245678865434"));
        assertThat(result.getSalary(), is(new BigDecimal("40000.99")));
        assertThat(result.getFileIdentifier(), is("REPORT.dat"));
        assertThat(result.getLayoutId(), is("001"));
        assertThat(result.getName(), is("Paulo"));

        then(customerService).should(never()).saveCustomer(any());
        then(saleService).should(never()).saveSale(any());
    }

    @Test
    public void givenOtherLineContent_shouldThrowError() {
    }


}