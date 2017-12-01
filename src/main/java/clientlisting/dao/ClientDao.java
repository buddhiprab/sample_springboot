package clientlisting.dao;

import clientlisting.model.Client;
import clientlisting.model.Holding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ClientDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Client> findByCustId(String id) {
        log.debug("findByCustId id:{}", id);
        String sql = "select custid, customername, custdocno, custsegcode, custregholdctrycode, custciprtng, custcipstat, cipassessdt, (case when (custaioptind='0') then 'N' else 'Y' end) as custaioptind, custarmcode, custtotalcasabal, custinvavlcash, noncustinvavlcash,astclslvl2code, mdllvl2allocamt, mdllvl2allocpercent, actllvl2allocamt, actllvl2allocpercent, lvl2portgappercent, lvl2portgapamount, fundAUM, bondAUM, casa as custCasa, tdmat as custTdmat, tddet as custTd from custdtl_lst_custid where rowkey like ?";
        return findById(id, sql);
    }

    public List<Client> findByBankId(String id) {
        log.debug("findByBankId id:{}", id);
        String sql = "select custid, customername, custdocno, custsegcode, custregholdctrycode, custciprtng, custcipstat, cipassessdt, (case when (custaioptind='0') then 'N' else 'Y' end) as custaioptind, custarmcode, custtotalcasabal, custinvavlcash, noncustinvavlcash,astclslvl2code, mdllvl2allocamt, mdllvl2allocpercent, actllvl2allocamt, actllvl2allocpercent, lvl2portgappercent, lvl2portgapamount, fundAUM, bondAUM, casa as custCasa, tdmat as custTdmat, tddet as custTd from custdtl_lst_bankid where rowkey like ?";
        return findById(id, sql);
    }

    public List<Client> findById(String id, String sqlQuery) {
        List clientsList = jdbcTemplate.query(sqlQuery, new Object[]{id + "%"}, new ResultSetExtractor<List>() {

            public List extractData(ResultSet rs) throws SQLException {
                Map<String, Client> clients = new HashMap<>();
                while (rs.next()) {
                    String custid = rs.getString("custid");
                    String custciprtng = rs.getString("custciprtng");
                    String custcipstat = rs.getString("custcipstat");
                    String clientKey = custid + "_" + custciprtng + "_" + custcipstat;
                    clients = updateClients(clients, clientKey, rs);
                }
                List newClients = new ArrayList(clients.values());
                return newClients;
            }
        });
        return clientsList;
    }

    public Map<String, Client> updateClients(Map<String, Client> clients, String clientKey, ResultSet rs) throws SQLException {
        Client client = clients.get(clientKey);
        if (client == null) {
            client = populateClient(rs);
            clients.put(clientKey, client);
        }
        Holding holding = populateHolding(rs);
        client.addToHoldings(holding);
        return clients;
    }

    public Client populateClient(ResultSet rs) throws SQLException {
        String custid = rs.getString("custid");
        String custciprtng = rs.getString("custciprtng");
        String custcipstat = rs.getString("custcipstat");
        String customername = rs.getString("customername");
        String custdocno = rs.getString("custdocno");
        String custsegcode = rs.getString("custsegcode");
        String custregholdctrycode = rs.getString("custregholdctrycode");
        String cipassessdt = rs.getString("cipassessdt");
        String custaioptind = rs.getString("custaioptind");
        String custarmcode = rs.getString("custarmcode");
        String noncustinvavlcash = rs.getString("noncustinvavlcash");
        String fundAUM = rs.getString("fundAUM");
        String bondAUM = rs.getString("bondAUM");
        String custCasa = rs.getString("custCasa");
        String custTdmat = rs.getString("custTdmat");
        String custTd = rs.getString("custTd");
        String custinvavlcash = rs.getString("custinvavlcash");

        custinvavlcash = getValueforNull(custinvavlcash);
        custCasa = getValueforNull(custCasa);
        custTdmat = getValueforNull(custTdmat);
        custTd = getValueforNull(custTd);
        String custtotalcasabal = getCusttotalcasabal(custCasa, custTdmat, custTd);
        fundAUM = getValueforNull(fundAUM);
        bondAUM = getValueforNull(bondAUM);
        custCasa = getValueforNull(custCasa);
        custTdmat = getValueforNull(custTdmat);
        custTd = getValueforNull(custTd);

        Client client = new Client();
        String clientKey = custid + "-" + custciprtng + "-" + custcipstat;
        client.setId(clientKey);
        client.setCustId(custid);
        client.setCustName(customername);
        client.setCustDocNo(custdocno);
        client.setCustSegCode(custsegcode);
        client.setCustRegHldCtrCde(custregholdctrycode);
        client.setCustCipRtng(custciprtng);
        client.setCustCipStat(custcipstat);
        client.setCipAssessDt(cipassessdt);
        client.setCustAioptInd(custaioptind);
        client.setCustArmCde(custarmcode);
        client.setCustTotalCasaBal(custtotalcasabal);
        client.setCustInvAvlCash(custinvavlcash);
        client.setNonCustInvAvlCash(noncustinvavlcash);
        client.setFundAum(fundAUM);
        client.setBondAum(bondAUM);
        client.setCustCasa(custCasa);
        client.setCustTdmat(custTdmat);
        client.setCustTd(custTd);
        return client;
    }

    public Holding populateHolding(ResultSet rs) throws SQLException {
        String custid = rs.getString("custid");
        String custciprtng = rs.getString("custciprtng");
        String astclslvl2code = rs.getString("astclslvl2code");
        String mdllvl2allocamt = rs.getString("mdllvl2allocamt");
        String mdllvl2allocpercent = rs.getString("mdllvl2allocpercent");
        String actllvl2allocamt = rs.getString("actllvl2allocamt");
        String actllvl2allocpercent = rs.getString("actllvl2allocpercent");
        String lvl2portgappercent = rs.getString("lvl2portgappercent");
        String lvl2portgapamount = rs.getString("lvl2portgapamount");

        Holding holding = new Holding();
        String holdingKey = custid + "-" + custciprtng + "-" + astclslvl2code;
        holding.setId(holdingKey);
        holding.setCustId(custid);
        holding.setAstClsLvl2Code(astclslvl2code);
        holding.setMdlLvl2AllocAmt(mdllvl2allocamt);
        holding.setMdlLvl2AllocPercent(mdllvl2allocpercent);
        holding.setActlLvl2AllocAmt(actllvl2allocamt);
        holding.setActlLvl2AllocPercent(actllvl2allocpercent);
        holding.setLvl2PortGapPercent(lvl2portgappercent);
        holding.setLvl2PortGapAmount(lvl2portgapamount);
        return holding;
    }

    public String getValueforNull(String value) {
        return value == null ? "0" : value;
    }

    public String getCusttotalcasabal(String custCasa, String custTdmat, String custTd) {
        return String.format("%.2f", (Double.parseDouble(custCasa) + Double.parseDouble(custTdmat) + Double.parseDouble(custTd)));
    }
}
