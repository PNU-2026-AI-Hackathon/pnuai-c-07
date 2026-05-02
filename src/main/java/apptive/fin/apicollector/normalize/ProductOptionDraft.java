package apptive.fin.apicollector.normalize;

import java.math.BigDecimal;

public record ProductOptionDraft(
        String intrRateType,
        String intrRateTypeName,
        Integer saveTerm,
        BigDecimal intrRate,
        BigDecimal intrRate2
) {
}
