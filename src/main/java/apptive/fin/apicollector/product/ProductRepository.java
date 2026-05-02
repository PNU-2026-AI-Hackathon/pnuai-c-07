package apptive.fin.apicollector.product;

import apptive.fin.apicollector.product.entity.Product;
import apptive.fin.apicollector.product.entity.ProductSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySourceAndProductCode(ProductSource source, String productCode);
}
