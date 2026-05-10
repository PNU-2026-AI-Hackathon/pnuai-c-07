package apptive.fin.search.service;

import apptive.fin.category.service.CategoryOptionService;
import apptive.fin.global.error.BusinessException;
import apptive.fin.search.CategoryIdEnum;
import apptive.fin.search.KeywordValueEnum;
import apptive.fin.search.SearchErrorCode;
import apptive.fin.search.dto.*;
import apptive.fin.search.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final EligibilityFilterService eligibilityFilterService;
    private final MatchScoreService matchScoreService;
    private final RateCalculatorService rateCalculatorService;

    public ProductSearchResultDto search(SearchRequestDto request){
        // 자격 필터링
        List<Product> eligible = eligibilityFilterService.filterEligible(request);

        // source별 분리
        List<Product> govList = eligible.stream()
                .filter(p -> p.getSource().getCode().equals("ONTONG")).toList();
        List<Product> bankList = eligible.stream()
                .filter(p -> p.getSource().getCode().equals("FSS")).toList();

        // 탭 A
        List<ProductMatchDto> govRanked = govList.stream()
                .map(p -> matchScoreService.score(p, request))
                .sorted(Comparator.comparingDouble(ProductMatchDto::totalScore).reversed())
                .toList();
        List<ProductMatchDto> bankRanked = bankList.stream()
                .map(p -> matchScoreService.score(p, request))
                .sorted(Comparator.comparingDouble(ProductMatchDto::totalScore).reversed())
                .toList();

        // 탭 B
        List<ProductRateDto> allRated = Stream.concat(govList.stream(), bankList.stream())
                .map(p -> rateCalculatorService.calculate(p, request)).toList();

        List<ProductRateDto> rateRanked = allRated.stream()
                .filter(r -> !r.isSubscription())
                .sorted(Comparator.comparingDouble(ProductRateDto::achievableRate).reversed())
                .toList();

        List<ProductRateDto> subscriptions = allRated.stream()
                .filter(ProductRateDto::isSubscription)
                .toList();

        // TODO : 계산 로직 추가 후 최종 완성할 부분
        return ProductSearchResultDto.builder()
                .governmentRanked(govRanked)
                .bankRanked(bankRanked)
                .rateRanked(rateRanked)
                .subscriptionProducts(subscriptions)
                .build();
    }



}
