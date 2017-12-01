package clientlisting.service;

import PIIApplication;
import clientlisting.dao.ClientDao;
import clientlisting.model.Client;
import common.constants.PIIConstants;
import exception.PIIException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = PIIApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ClientServiceTest {
    private List<Client> clients;

    private final long totalClientCount = 0;
    private final long pageNumber = 0;
    private final long pageSize = 0;

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;

    @Before
    public void setUp() {
        clients = new ArrayList<>();
        clients.add(new Client());
    }

    @Test
    public void findByCustIDSuccess() throws Exception {
        String id = "S12345678";
        when(clientDao.findByCustId(id)).thenReturn(clients);
        when(clientDao.findByIdCount(id)).thenReturn(totalClientCount);

        Map<String, Object> clientsMap = clientService.findByCustID(id, pageNumber, pageSize);
        assertEquals(clients, (List<Client>) clientsMap.get(PIIConstants.PII_PAYLOAD));
        assertEquals(totalClientCount, clientsMap.get(PIIConstants.PII_PAYLOAD_COUNT));
    }

    @Test(expected = PIIException.class)
    public void findByCustIDFailure() throws Exception {
        String id = "S12345678";
        Map<String, Object> clientsMap = clientService.findByCustID(id, pageNumber, pageSize);
    }

    @Test
    public void findByBankIDSuccess() throws Exception {
        String id = "S12345678";
        when(clientDao.findByBankId(id)).thenReturn(clients);
        when(clientDao.findByIdCount(id)).thenReturn(totalClientCount);

        Map<String, Object> clientsMap = clientService.findByBankID(id, pageNumber, pageSize);
        assertEquals(clients, (List<Client>) clientsMap.get(PIIConstants.PII_PAYLOAD));
        assertEquals(totalClientCount, clientsMap.get(PIIConstants.PII_PAYLOAD_COUNT));
    }

    @Test(expected = PIIException.class)
    public void findByBankIDFailure() throws Exception {
        String id = "S12345678";
        Map<String, Object> clientsMap = clientService.findByBankID(id, pageNumber, pageSize);
    }

}