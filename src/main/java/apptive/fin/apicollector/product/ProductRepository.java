package apptive.fin.apicollector.product;

import apptive.fin.apicollector.Source;
import apptive.fin.apicollector.product.entity.Product;
import apptive.fin.apicollector.product.entity.ProductSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySourceAndProductCode(ProductSource source, String productCode);

    @Query("""
        update Product p
            set p.isJoinable = false
           where p.source = :productSource
           and exists(
               select pr.id
                   from ProductRaw pr
                   where pr.source = :source
                       and pr.externalId = p.productCode
                       and pr.lastSeenAt < :lastSeen
               )
    """)
    @Modifying
    int disableBySourceAndLastSeenBefore(ProductSource productSource, Source source, Instant lastSeen);
}
