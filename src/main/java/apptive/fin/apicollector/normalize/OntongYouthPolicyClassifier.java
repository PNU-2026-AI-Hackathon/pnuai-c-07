package apptive.fin.apicollector.normalize;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OntongYouthPolicyClassifier extends AbstractProductNormalizer {

    private static final String FINANCE_CATEGORY = "취약계층 및 금융지원";
    private static final String SUBSIDY_KEYWORD = "보조금";
    private static final String LOAN_KEYWORD = "대출";
    private static final List<String> LOAN_METHOD_CODES = List.of("42003", "42007");
//    private static final List<String> FINANCIAL_KEYWORDS = List.of(
//            "저축",
//            "적금",
//            "예금",
//            "자산형성",
//            "매칭",
//            "장려금",
//            "기여금",
//            "적립",
//            "내일채움",
//            "내일저축",
//            "미래적금",
//            "납입",
//            "목돈",
//            "비과세",
//            "청약",
//            "주택드림",
//            "분양",
//            "통장"
//    );

    private static final Map<String, Integer> FINANCIAL_KEY_MAP = Map.ofEntries(
            Map.entry("통장", 60),
            Map.entry("적금", 50),
            Map.entry("예금", 50),
            Map.entry("저축", 3),
            Map.entry("자산형성", 4),
            Map.entry("계좌", 4),
            Map.entry("내일저축", 70),
            Map.entry("미래적금", 70),
            Map.entry("청년도약계좌", 70),
            Map.entry("내일채움", 50),
            Map.entry("주택드림", 60),
            Map.entry("청약", 3),
            Map.entry("비과세", 4),
            Map.entry("기여금", 3),
            Map.entry("공제", 4),
            Map.entry("적립", 2),
            Map.entry("납입", 2),
            Map.entry("매칭", 1),
            Map.entry("분양", 1)
    );




    public ProductClassification classify(JsonNode policy) {
//        if (!isFinanceCandidate(policy)) {
//            return ProductClassification.EXCLUDED;
//        }

        if (isLoan(policy)) {
            return ProductClassification.LOAN_EXCLUDED;
        }

//        if (hasFinancialKeyword(policy)) {
//            return ProductClassification.FINANCIAL_PRODUCT;
//        }
        if (financeScore(policy) >= 6) {
            return ProductClassification.FINANCIAL_PRODUCT;
        }

        return ProductClassification.UNCLASSIFIED;
    }

    private boolean isFinanceCandidate(JsonNode policy) {
        String category = text(policy, "mclsfNm");
        String keywords = text(policy, "plcyKywdNm");

        return FINANCE_CATEGORY.equals(category);
//                || contains(keywords, SUBSIDY_KEYWORD);
    }

    private boolean isLoan(JsonNode policy) {
        String keywords = text(policy, "plcyKywdNm");
        String methodCode = text(policy, "plcyPvsnMthdCd");

        return contains(keywords, LOAN_KEYWORD)
                || LOAN_METHOD_CODES.stream().anyMatch(code -> hasCode(methodCode, code));
    }

//    private boolean hasFinancialKeyword(JsonNode policy) {
//        String supportContent = text(policy, "plcySprtCn");
//        return FINANCIAL_KEYWORDS.stream().anyMatch(keyword -> contains(supportContent, keyword));
//    }

    private int financeScore(JsonNode policy) {
        Map<String, Integer> keywordsMap = new HashMap<>();
        String supportContent = text(policy, "plcySprtCn");
        String category = text(policy, "mclsfNm");
        String keywords = text(policy, "plcyKywdNm");
        String name = text(policy, "plcyNm");

//        for (String keyword : LOAN_METHOD_CODES) {
//            keywordsMap.put(keyword, StringUtils.countOccurrencesOf(supportContent, keyword));
//        }
        return FINANCIAL_KEY_MAP
                .keySet()
                .stream()
                .map((keyword)->StringUtils.countOccurrencesOf(name, keyword) * FINANCIAL_KEY_MAP.get(keyword))
                .reduce(Integer::sum)
                .orElse(0);

    }

    private boolean hasCode(String rawCode, String targetCode) {
        return rawCode != null && rawCode.replaceFirst("^0+", "").equals(targetCode);
    }

    private boolean contains(String value, String token) {
        return value != null && value.contains(token);
    }
}
