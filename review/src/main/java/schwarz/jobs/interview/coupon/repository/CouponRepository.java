package schwarz.jobs.interview.coupon.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import schwarz.jobs.interview.coupon.domain.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    Optional<CouponEntity> findByCode(final String code);

    boolean existsByCode(final String code);
}
