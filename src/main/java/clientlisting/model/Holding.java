package clientlisting.model;

import io.katharsis.resource.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = "holding")
public class Holding {
    @JsonApiId
    private String id;

    private String custId;
    private String astClsLvl2Code;
    private String mdlLvl2AllocAmt;
    private String mdlLvl2AllocPercent;
    private String actlLvl2AllocAmt;
    private String actlLvl2AllocPercent;
    private String lvl2PortGapPercent;
    private String lvl2PortGapAmount;
}
