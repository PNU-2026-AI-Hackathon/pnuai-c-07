package apptive.fin.apicollector.raw;

import apptive.fin.apicollector.Source;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRawRepository extends JpaRepository<ProductRaw, Long> {
    Optional<ProductRaw> findBySourceAndExternalId(
            Source source, String externalId
    );

    List<ProductRaw> findAllBySourceIn(Collection<Source> sources);

    @Query("""
        SELECT r
            FROM ProductRaw r
            WHERE r.source in :sources
                and r.id > :lastSeenId
                and (
                    r.normalizedAt is null
                    or r.normalizerVersion is null
                    or r.normalizerVersion < :normalizerVersion
                )
            order by r.id asc
    """)
    List<ProductRaw> findNextNeedNormalize(
            Collection<Source> sources,
            Long lastSeenId,
            int normalizerVersion,
            Pageable pageable
    );

    List<ProductRaw> findAllBySourceAndLastSeenAtBefore(
            Source source,
            Instant lastSeenAt
    );

}
