package apptive.fin.apicollector.devtools.report;

import apptive.fin.apicollector.Mode;
import apptive.fin.apicollector.Source;
import apptive.fin.apicollector.config.CollectorProperties;
import apptive.fin.apicollector.devtools.support.DevtoolPaths;
import apptive.fin.apicollector.normalize.MonthlyLimitExtractor;
import apptive.fin.apicollector.normalize.OntongYouthPolicyClassifier;
import apptive.fin.apicollector.normalize.OntongYouthProductNormalizer;
import apptive.fin.apicollector.normalize.ProductDraft;
import apptive.fin.apicollector.raw.ProductRaw;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OntongYouthFilterReport {

    private static final Path REPORT_PATH = DevtoolPaths.REPORTS_DIR.resolve(
            "ontong-youth-financial-products.jsonl"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OntongYouthProductNormalizer normalizer = new OntongYouthProductNormalizer(
            objectMapper,
            properties(),
            new OntongYouthPolicyClassifier(),
            new MonthlyLimitExtractor()
    );

    public static void main(String[] args) throws IOException {
        OntongYouthFilterReport report = new OntongYouthFilterReport();
        List<FilteredProduct> products = report.filterFinancialProducts();
        report.write(products);
        System.out.printf(
                "Wrote %d Ontong Youth financial products to %s%n",
                products.size(),
                REPORT_PATH
        );
    }

    private List<FilteredProduct> filterFinancialProducts() throws IOException {
        return loadOntongYouthRawProducts().stream()
                .map(normalizer::normalize)
                .filter(ProductDraft::shouldSaveProduct)
                .map(draft -> new FilteredProduct(
                        draft.productCode(),
                        draft.productName(),
                        draft.providerName()
                ))
                .toList();
    }

    private void write(List<FilteredProduct> products) throws IOException {
        Files.createDirectories(REPORT_PATH.getParent());
        Files.writeString(REPORT_PATH, toJsonLines(products));
    }

    private String toJsonLines(List<FilteredProduct> products) throws IOException {
        StringBuilder builder = new StringBuilder("# count: " + products.size() + "\n");
        for (FilteredProduct product : products) {
            builder.append("{\"productCode\":\"")
                    .append(jsonEscape(product.productCode()))
                    .append("\",\"productName\":\"")
                    .append(jsonEscape(product.productName()))
                    .append("\",\"providerName\":\"")
                    .append(jsonEscape(product.providerName()))
                    .append("\"}")
                    .append('\n');
        }
        return builder.toString();
    }

    private String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        return value;
//        StringBuilder escaped = new StringBuilder(value.length());
//        for (int i = 0; i < value.length(); i++) {
//            char c = value.charAt(i);
//            switch (c) {
//                case '"' -> escaped.append("\\\"");
//                case '\\' -> escaped.append("\\\\");
//                case '\b' -> escaped.append("\\b");
//                case '\f' -> escaped.append("\\f");
//                case '\n' -> escaped.append("\\n");
//                case '\r' -> escaped.append("\\r");
//                case '\t' -> escaped.append("\\t");
//                default -> {
//                    if (c < 0x20 || c > 0x7e) {
//                        escaped.append(String.format("\\u%04x", (int) c));
//                    }
//                    else {
//                        escaped.append(c);
//                    }
//                }
//            }
//        }
//        return escaped.toString();
    }

    private List<ProductRaw> loadOntongYouthRawProducts() throws IOException {
        JsonNode rows;
        try (InputStream inputStream = getClass().getResourceAsStream("/product_raw.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("product_raw.json not found on classpath");
            }
            rows = objectMapper.readTree(inputStream);
        }

        List<ProductRaw> products = new ArrayList<>();
        for (JsonNode row : rows) {
            if (Source.ONTONG_YOUTH.name().equals(row.path("source").asString())) {
                products.add(new ProductRaw(
                        Source.ONTONG_YOUTH,
                        row.path("external_id").asString(),
                        row.path("content_hash").asString(),
                        row.path("raw_json").asString()
                ));
            }
        }
        return products;
    }

    private CollectorProperties properties() {
        return new CollectorProperties(
                true,
                Source.ALL,
                Mode.NORMALIZE_ONLY,
                3,
                500,
                7,
                new CollectorProperties.OntongYouth("http://localhost", "key", 100),
                new CollectorProperties.Fss("http://localhost", "key", 100)
        );
    }

    private record FilteredProduct(
            String productCode,
            String productName,
            String providerName
    ) {}
}
