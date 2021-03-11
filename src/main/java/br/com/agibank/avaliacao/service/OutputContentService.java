package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.model.Customer;
import br.com.agibank.avaliacao.model.Sale;
import br.com.agibank.avaliacao.model.Salesman;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
@AllArgsConstructor
public class OutputContentService {

    private static final String PATH_PROPERTY = "user.home";
    private static final String RELATIVE_PATH = "/data/out/";
    private static final String EXTENSION = ".done.dat";
    private final SaleService saleService;
    private final SalesmanService salesmanService;
    private final CustomerService customerService;
    private final FileStreamingService fileStreamingService;

    public void generateReport(final String fileName) {
        final String path = System.getProperty(PATH_PROPERTY) + RELATIVE_PATH + fileName + EXTENSION;

        final List<Sale> saleDataList = saleService.getSalesByFileIdentifier(fileName);
        final List<Customer> customerDataList = customerService.getCustomersByFileIdentifier(fileName);
        final List<Salesman> salesmanDataList = salesmanService.getSalesByFileIdentifier(fileName);

        final Set<String> set = new HashSet<>(customerDataList.size());
        final int customersQuantity = (int) customerDataList
                .stream()
                .filter(customerData -> set.add(customerData.getCnpj())).count();

        final int salesmanQuantity = (int) salesmanDataList
                .stream()
                .filter(salesmanData -> set.add(salesmanData.getCpf())).count();

        final Optional<Sale> mostExpensiveSale = Optional.of(saleDataList
                .stream()
                .max(Comparator.comparing(Sale::getTotalAmount))
                .orElseThrow(() -> new RuntimeException("Could not find most expansive sale.")));

        final String mostExpensiveSaleId = mostExpensiveSale.get().getSaleId();

        final String worseSalesMan = saleService.getSmallerAmountSumSalesmanName();

        final String content = String.format("File id: %s %nCustomers: %o%nSalesmans:%o%nMost expensive sale id: %s%nWorse salesman: %s",
                fileName,
                customersQuantity,
                salesmanQuantity,
                mostExpensiveSaleId,
                worseSalesMan);

        fileStreamingService.writeFileAsynchronously(path, content);
    }

}
