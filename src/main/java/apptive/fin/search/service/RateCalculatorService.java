package apptive.fin.search.service;

import apptive.fin.search.KeywordValueEnum;
import apptive.fin.search.dto.ProductRateDto;
import apptive.fin.search.dto.SearchRequestDto;
import apptive.fin.search.entity.Product;
import apptive.fin.search.entity.ProductProperty;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class RateCalculatorService {
    public ProductRateDto calculate(Product p, SearchRequestDto request) {
        ProductProperty bestProperty = p.getProperties().stream()
                .max(Comparator.comparingDouble(this::effectiveRate))
                .orElse(null);

        boolean hasRateOption = p.getProperties().stream()
                .anyMatch(property -> property.getBaseRate() != null || property.getMaxRate() != null);
        if (hasRateOption) {
            return ProductRateDto.builder()
                    .productId(p.getId())
                    .productPropertyId(bestProperty != null ? bestProperty.getId() : null)
                    .productName(p.getProductName())
                    .providerName(providerName(bestProperty))
                    .source(p.getSource().getCode())
                    .baseRate(baseRate(bestProperty))
                    .achievableRate(bestProperty != null ? effectiveRate(bestProperty) : 0.0)
                    .isSubscription(false)
                    .build();
        }

        ProductProperty subscriptionProperty = p.getProperties().stream()
                .filter(property -> property.getKeywords().stream()
                        .anyMatch(k -> k.getKeywordCode() == KeywordValueEnum.INTEREST_SAVINGS))
                .findFirst()
                .orElse(null);

        if (subscriptionProperty != null) {
            return ProductRateDto.builder()
                    .productId(p.getId())
                    .productPropertyId(subscriptionProperty.getId())
                    .productName(p.getProductName())
                    .providerName(providerName(subscriptionProperty))
                    .source(p.getSource().getCode())
                    .isSubscription(true)
                    .subscriptionNote("청약: 금리 비교 대상 아님")
                    .build();
        }

        double baseRate = baseRate(bestProperty);
        double maxRate = bestProperty != null ? effectiveRate(bestProperty) : baseRate;

        return ProductRateDto.builder()
                .productId(p.getId())
                .productPropertyId(bestProperty != null ? bestProperty.getId() : null)
                .productName(p.getProductName())
                .providerName(providerName(bestProperty))
                .source(p.getSource().getCode())
                .baseRate(baseRate)
                .achievableRate(maxRate)
                .isSubscription(false)
                .build();
    }

    private double baseRate(ProductProperty property) {
        return property != null && property.getBaseRate() != null
                ? property.getBaseRate().doubleValue()
                : 0.0;
    }

    private double effectiveRate(ProductProperty property) {
        if (property.getMaxRate() != null) {
            return property.getMaxRate().doubleValue();
        }
        if (property.getBaseRate() != null) {
            return property.getBaseRate().doubleValue();
        }
        return 0.0;
    }

    private String providerName(ProductProperty property) {
        return property != null && property.getProvider() != null
                ? property.getProvider().getName()
                : null;
    }
}
