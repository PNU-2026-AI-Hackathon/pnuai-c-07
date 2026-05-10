package apptive.fin.search;

import apptive.fin.search.dto.DetailedOptionsDto;
import apptive.fin.search.dto.ProductMatchDto;
import apptive.fin.search.dto.SearchRequestDto;
import apptive.fin.search.entity.Product;
import apptive.fin.search.entity.ProductProperty;
import apptive.fin.search.entity.ProductSource;
import apptive.fin.search.entity.Provider;
import apptive.fin.search.service.MatchScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MatchScoreServiceTest {

    private final MatchScoreService matchScoreService = new MatchScoreService();

    @Test
    void 월저축목표가_null이어도_예외없이_저축액점수를_제외한다() {
        Product product = new Product();
        ProductSource source = new ProductSource();
        Provider provider = new Provider();
        ProductProperty property = new ProductProperty();

        ReflectionTestUtils.setField(source, "code", "FSS");
        ReflectionTestUtils.setField(provider, "name", "테스트은행");
        ReflectionTestUtils.setField(property, "id", 10L);
        ReflectionTestUtils.setField(property, "provider", provider);
        ReflectionTestUtils.setField(property, "maxMonthlyLimit", 500_000L);
        ReflectionTestUtils.setField(property, "keywords", new ArrayList<>());
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(product, "productName", "청년우대적금");
        ReflectionTestUtils.setField(product, "source", source);
        ReflectionTestUtils.setField(product, "properties", new ArrayList<>(List.of(property)));

        SearchRequestDto request = new SearchRequestDto(
                List.of(),
                new DetailedOptionsDto(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        List.of()
                )
        );

        ProductMatchDto result = matchScoreService.score(product, request);

        assertThat(result.depositScore()).isZero();
        assertThat(result.productPropertyId()).isEqualTo(10L);
        assertThat(result.providerName()).isEqualTo("테스트은행");
    }
}
