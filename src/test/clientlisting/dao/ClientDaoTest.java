package clientlisting.dao;

import PIIApplication;
import clientlisting.model.Client;
import clientlisting.model.Holding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PIIApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ClientDaoTest {

    @Autowired
    private ClientDao clientDao;
    private final Logger log = LoggerFactory.getLogger(ClientDao.class);

    @Mock
    private ResultSet rs;

    @Test
    public void testPopulateClient() throws Exception {
        String custid = "01S2585257E", custciprtng = "4", custcipstat = "ACTIVE",
                customername = "XXX", custdocno = "XXX", custsegcode = "3",
                custregholdctrycode = "SG", cipassessdt = "2016-07-12",
                custaioptind = "2", custarmcode = "622", noncustinvavlcash = "0",
                fundAUM = "300.00", bondAUM = "200.00", custCasa = "1.00", custTdmat = "10.00",
                custTd = "100", custinvavlcash = "555.0", custtotalcasabal = "111.00";
        String clientKey = custid + "-" + custciprtng + "-" + custcipstat;

        when(rs.getString("custid")).thenReturn(custid);
        when(rs.getString("custciprtng")).thenReturn(custciprtng);
        when(rs.getString("custcipstat")).thenReturn(custcipstat);
        when(rs.getString("customername")).thenReturn(customername);
        when(rs.getString("custdocno")).thenReturn(custdocno);
        when(rs.getString("custsegcode")).thenReturn(custsegcode);
        when(rs.getString("custregholdctrycode")).thenReturn(custregholdctrycode);
        when(rs.getString("custaioptind")).thenReturn(custaioptind);
        when(rs.getString("cipassessdt")).thenReturn(cipassessdt);
        when(rs.getString("custarmcode")).thenReturn(custarmcode);
        when(rs.getString("noncustinvavlcash")).thenReturn(noncustinvavlcash);
        when(rs.getString("fundAUM")).thenReturn(fundAUM);
        when(rs.getString("bondAUM")).thenReturn(bondAUM);
        when(rs.getString("custCasa")).thenReturn(custCasa);
        when(rs.getString("custTdmat")).thenReturn(custTdmat);
        when(rs.getString("custTd")).thenReturn(custTd);
        when(rs.getString("custinvavlcash")).thenReturn(custinvavlcash);

        Client client = clientDao.populateClient(rs);

        assert(client.getId()).equals(clientKey);
        assert(client.getCustId()).equals(custid);
        assert(client.getCustName()).equals(customername);
        assert(client.getCustDocNo()).equals(custdocno);
        assert(client.getCustSegCode()).equals(custsegcode);
        assert(client.getCustRegHldCtrCde()).equals(custregholdctrycode);
        assert(client.getCustCipRtng()).equals(custciprtng);
        assert(client.getCustCipStat()).equals(custcipstat);
        assert(client.getCipAssessDt()).equals(cipassessdt);
        assert(client.getCustAioptInd()).equals(custaioptind);
        assert(client.getCustArmCde()).equals(custarmcode);
        assert(client.getCustTotalCasaBal()).equals(custtotalcasabal);
        assert(client.getCustInvAvlCash()).equals(custinvavlcash);
        assert(client.getNonCustInvAvlCash()).equals(noncustinvavlcash);
        assert(client.getFundAum()).equals(fundAUM);
        assert(client.getBondAum()).equals(bondAUM);
        assert(client.getCustCasa()).equals(custCasa);
        assert(client.getCustTdmat()).equals(custTdmat);
        assert(client.getCustTd()).equals(custTd);
    }
}