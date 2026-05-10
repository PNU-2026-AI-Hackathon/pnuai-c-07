package apptive.fin.search;

import apptive.fin.search.dto.DetailedOptionsDto;
import apptive.fin.search.dto.ProductRateDto;
import apptive.fin.search.dto.SearchRequestDto;
import apptive.fin.search.entity.Product;
import apptive.fin.search.entity.ProductKeyword;
import apptive.fin.search.entity.ProductProperty;
import apptive.fin.search.entity.ProductSource;
import apptive.fin.search.entity.Provider;
import apptive.fin.search.service.RateCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RateCalculatorServiceTest {

    private final RateCalculatorService rateCalculatorService = new RateCalculatorService();

    @Test
    void 기본금리와_최고금리로_가장_높은_달성가능금리를_계산한다() {
        Product product = createProduct("BANK001", "청년우대적금", "FSS");
        ReflectionTestUtils.setField(product, "properties", new ArrayList<>(List.of(
                createProperty("3.80", "4.50"),
                createProperty("3.50", "4.20")
        )));

        ProductRateDto result = rateCalculatorService.calculate(product, createRequest());

        assertThat(result.productId()).isEqualTo(1L);
        assertThat(result.productPropertyId()).isEqualTo(10L);
        assertThat(result.productName()).isEqualTo("청년우대적금");
        assertThat(result.providerName()).isEqualTo("테스트은행");
        assertThat(result.baseRate()).isEqualTo(3.8);
        assertThat(result.achievableRate()).isEqualTo(4.5);
        assertThat(result.isSubscription()).isFalse();
    }

    @Test
    void 최고금리가_없으면_기본금리를_달성가능금리로_사용한다() {
        Product product = createProduct("BANK002", "기본금리상품", "FSS");
        ReflectionTestUtils.setField(product, "properties", new ArrayList<>(List.of(
                createProperty("2.75", null)
        )));

        ProductRateDto result = rateCalculatorService.calculate(product, createRequest());

        assertThat(result.baseRate()).isEqualTo(2.75);
        assertThat(result.achievableRate()).isEqualTo(2.75);
    }

    @Test
    void 금리가_없는_청약상품은_금리비교_대상에서_제외한다() {
        Product product = createProduct("GOV001", "청약상품", "ONTONG");
        ProductKeyword keyword = new ProductKeyword();
        ProductProperty property = createProperty(null, null);
        ReflectionTestUtils.setField(keyword, "keywordCode", KeywordValueEnum.INTEREST_SAVINGS);
        ReflectionTestUtils.setField(property, "keywords", new ArrayList<>(List.of(keyword)));
        ReflectionTestUtils.setField(product, "properties", new ArrayList<>(List.of(property)));

        ProductRateDto result = rateCalculatorService.calculate(product, createRequest());

        assertThat(result.isSubscription()).isTrue();
        assertThat(result.productPropertyId()).isEqualTo(10L);
        assertThat(result.providerName()).isEqualTo("테스트은행");
        assertThat(result.subscriptionNote()).isEqualTo("청약: 금리 비교 대상 아님");
    }

    private Product createProduct(String code, String name, String sourceCode) {
        ProductSource source = new ProductSource();
        ReflectionTestUtils.setField(source, "code", sourceCode);

        Product product = new Product();
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "productCode", code);
        ReflectionTestUtils.setField(product, "productName", name);
        ReflectionTestUtils.setField(product, "source", source);
        return product;
    }

    private ProductProperty createProperty(String baseRate, String maxRate) {
        ProductProperty property = new ProductProperty();
        Provider provider = new Provider();
        ReflectionTestUtils.setField(provider, "name", "테스트은행");
        ReflectionTestUtils.setField(property, "id", 10L);
        ReflectionTestUtils.setField(property, "provider", provider);
        ReflectionTestUtils.setField(property, "keywords", new ArrayList<>());
        if (baseRate != null) {
            ReflectionTestUtils.setField(property, "baseRate", new BigDecimal(baseRate));
        }
        if (maxRate != null) {
            ReflectionTestUtils.setField(property, "maxRate", new BigDecimal(maxRate));
        }
        return property;
    }

    private SearchRequestDto createRequest() {
        return new SearchRequestDto(
                List.of(),
                new DetailedOptionsDto(null, null, null, null, null, null, null, null, null, null, List.of())
        );
    }
}
