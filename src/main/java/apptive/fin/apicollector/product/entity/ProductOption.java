package apptive.fin.apicollector.product.entity;

import apptive.fin.apicollector.normalize.ProductOptionDraft;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String intrRateType;

    private String intrRateTypeNm;

    @Column(nullable = false)
    private Integer saveTrm;

    @Column(precision = 5, scale = 2)
    private BigDecimal intrRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal intrRate2;

    private ProductOption(Product product, ProductOptionDraft draft) {
        this.product = product;
        this.intrRateType = draft.intrRateType();
        this.intrRateTypeNm = draft.intrRateTypeName();
        this.saveTrm = draft.saveTerm();
        this.intrRate = draft.intrRate();
        this.intrRate2 = draft.intrRate2();
    }

    public static ProductOption create(Product product, ProductOptionDraft draft) {
        return new ProductOption(product, draft);
    }
}
