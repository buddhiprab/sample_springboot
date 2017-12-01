package clientlisting.model;


import java.util.ArrayList;
import java.util.List;

import io.katharsis.resource.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = "clients")
public class Client {
    @JsonApiId
    private String id;

    private String custId;
    private String bankId;
    private String custName;
    private String custDocNo;
    private String custSegCode;
    private String custRegHldCtrCde;
    private String custCipRtng;
    private String custCipStat;
    private String cipAssessDt;
    private String custAioptInd;
    private String custArmCde;
    private String custTotalCasaBal;
    private String custInvAvlCash;
    private String nonCustInvAvlCash;
    private String fundAum;
    private String bondAum;
    private String custCasa;
    private String custTdmat;
    private String custTd;

    @JsonApiRelation(lookUp = LookupIncludeBehavior.NONE, serialize = SerializeType.EAGER)
    private List<Holding> holding = new ArrayList<>();

    public void addToHoldings(Holding holding) {
        this.holding.add(holding);
    }
}
