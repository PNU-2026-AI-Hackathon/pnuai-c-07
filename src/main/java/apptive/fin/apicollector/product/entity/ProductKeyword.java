package apptive.fin.apicollector.product.entity;

import apptive.fin.apicollector.product.KeywordValueEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "product_keyword",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_keyword_product_keyword_code",
                        columnNames = {"product_id", "keyword_code"}
                )
        }
)
public class ProductKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "keyword_code", nullable = false)
    private KeywordValueEnum keywordCode;

    private ProductKeyword(Product product, KeywordValueEnum keywordCode) {
        this.product = product;
        this.keywordCode = keywordCode;
    }

    public static ProductKeyword create(Product product, KeywordValueEnum keywordCode) {
        return new ProductKeyword(product, keywordCode);
    }
}
