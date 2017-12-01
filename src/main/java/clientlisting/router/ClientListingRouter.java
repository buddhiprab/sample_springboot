package clientlisting.router;

import common.constants.PIIConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientListingRouter extends FatJarRouter {

    @Autowired
    CamelContext camelContext;

    @Override
    public void configure() {
        camelContext.setStreamCaching(true);

        from("direct:clientListingRouter").id("ClientListingRoute").routeId("ClientListingRoute")
                .log("CustId:${header.custId},BankId: ${header.bankId} ")
                .choice()
                .when(header(PIIConstants.BANK_ID).isNotNull()).id("BankId").routeId("BankId")
                .to("bean:clientService?method=findByBankID(${header.bankId},${header.pageNumber},${header.pageSize})")
                .when(header(PIIConstants.CUST_ID).isNotNull()).id("CustId").routeId("CustId")
                .to("bean:clientService?method=findByCustID(${header.custId},${header.pageNumber},${header.pageSize})")
                .when(header("relId").isNotNull()).routeId("RelId")
                .to("bean:clientService?method=findByCustID(${header.relId},${header.pageNumber},${header.pageSize})")
                .otherwise().id("Otherwise")
                .to("bean:clientService?method=badRequest()");
    }
}
