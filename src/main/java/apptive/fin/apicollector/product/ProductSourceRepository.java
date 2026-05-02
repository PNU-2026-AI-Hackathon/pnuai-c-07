package apptive.fin.apicollector.product;

import apptive.fin.apicollector.product.entity.ProductSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSourceRepository extends JpaRepository<ProductSource, Long> {

    Optional<ProductSource> findByCode(String code);
}
