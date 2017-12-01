package clientlisting.controller;

import com.sc.csl.retail.core.web.CSLRequestContext;
import clientlisting.model.Client;
import common.constants.PIIConstants;
import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Priority(Integer.MAX_VALUE)
public class ClientController extends ResourceRepositoryBase<Client, Long> implements ClientRepository {

    @EndpointInject(uri = "direct:clientListingRouter")
    private FluentProducerTemplate producer;

    @Autowired
    private CSLRequestContext cslRequestContext;

    public ClientController() {
        super(Client.class);
    }

    @Override
    public synchronized ClientList findAll(QuerySpec querySpec) {
        try {
            querySpec.getFilters().stream().forEach(filterSpec -> {
                String value = (String) filterSpec.getValue();
                String key = filterSpec.getAttributePath().get(0);
                log.info("Key - {}, Value - {}", key, value);
                producer.withHeader(key, value);
            });
            producer.withHeader(PIIConstants.REL_ID, cslRequestContext.getRelId());
            log.info("Requestor Rel Id: {} ", cslRequestContext.getRelId());
            long offset = querySpec.getOffset();
            long limit = querySpec.getLimit();
            producer.withHeader("pageNumber", offset);
            producer.withHeader("pageSize", limit);
            Map<String, Object> clientMap = producer.request(Map.class);
            ClientList list = null;
            if (clientMap != null) {
                list = new ClientList();
                ClientListMeta meta = new ClientListMeta();
                ClientListLinks links = new ClientListLinks();
                list.addAll((List<Client>) clientMap.get(PIIConstants.PII_PAYLOAD));
                meta.setTotalResourceCount((Long) clientMap.get(PIIConstants.PII_PAYLOAD_COUNT));
                list.setMeta(meta);
                list.setLinks(links);
            }
            return list;
        } finally {
            producer.clearHeaders();
            producer.clearBody();
        }
    }
}
