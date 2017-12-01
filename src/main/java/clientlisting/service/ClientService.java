package clientlisting.service;

import clientlisting.dao.ClientDao;
import clientlisting.model.Client;
import common.constants.PIIConstants;
import exception.PIIException;
import io.katharsis.repository.response.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClientService {

    @Autowired
    private ClientDao clientDao;

    @Transactional(readOnly = true)
    public Map<String, Object> findByCustID(String id, Long pageNumber, Long pageSize) {
        log.info("findByCustId. Id:{}", id);
        if (id.isEmpty()) {
            badRequest();
        }
        List<Client> clients = clientDao.findByCustId(id);
        if (clients != null && !clients.isEmpty()) {
            log.debug("Size of the CustID collection size:{}", clients.size());
        } else {
            throw new PIIException("Requested CustId was not found on the server", HttpStatus.NOT_FOUND_404, id);
        }
        long totalClientCount = clientDao.findByIdCount(id);
        Map<String, Object> clientsMap = new HashMap<>();
        clientsMap.put(PIIConstants.PII_PAYLOAD, clients);
        clientsMap.put(PIIConstants.PII_PAYLOAD_COUNT, totalClientCount);
        return clientsMap;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findByBankID(String id, Long pageNumber, Long pageSize) {
        log.info("findByBankID. Id:{}", id);
        if (id.isEmpty()) {
            badRequest();
        }
        List<Client> clients = clientDao.findByBankId(id);
        if (clients != null && !clients.isEmpty()) {
            log.debug("Size of the BankID collection size:{}", clients);
        } else {
            throw new PIIException("Requested BankId was not found on the server", HttpStatus.NOT_FOUND_404, id);
        }
        long totalClientCount = clientDao.findByIdCount(id);
        Map<String, Object> clientsMap = new HashMap<>();
        clientsMap.put(PIIConstants.PII_PAYLOAD, clients);
        clientsMap.put(PIIConstants.PII_PAYLOAD_COUNT, totalClientCount);
        return clientsMap;
    }

    @Transactional(readOnly = true)
    public void badRequest() {
        throw new PIIException("RM id or client id is mandatory", HttpStatus.BAD_REQUEST_400, "No Identifier");
    }

}
