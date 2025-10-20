package schwarz.jobs.interview.coupon.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.domain.CouponEntity;
import schwarz.jobs.interview.coupon.repository.CouponRepository;
import schwarz.jobs.interview.coupon.dto.BasketDTO;
import schwarz.jobs.interview.coupon.dto.CouponDTO;
import schwarz.jobs.interview.coupon.dto.CouponsRequestDTO;
import schwarz.jobs.interview.coupon.exception.CouponAlreadyExistsException;
import schwarz.jobs.interview.coupon.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.mapper.CouponMapper;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;


    public Optional<CouponEntity> getCoupon(final String code) {
        log.debug("CouponService.getCoupon()");

        if (StringUtils.isBlank(code)) {
            log.debug("Code parameter cannot be empty: {}", code);
            throw new ValidationException("Code parameter cannot be empty!");
        }
        
        if (code.length() > 250) {
            log.debug("Code parameter is too long: {}", code);
            throw new ValidationException("Code parameter too long!");
        }

        CouponEntity coupon = 
                couponRepository.findByCode(code)
                        .orElseThrow(() -> new CouponNotFoundException("Coupon not found for code!"));

        /*if (!coupon.isPresent()) {
            log.debug("No coupon found for code {}", code);
            //throw new BusinessProcessException(COUPON_NOT_FOUND_FOR_CODE, code);
            throw new CouponNotFoundException("No coupon found for code!");
        }*/

        return Optional.ofNullable(coupon);
    }

    public Optional<BasketDTO> applyBasket(final BasketDTO basketDTO, final String code) {
        log.debug("Applying basket with code: {}", code);
        
        return getCoupon(code)
            .filter(coupon -> isCouponApplicable(basketDTO.getValue(), coupon.getDiscount()))
            .map(coupon -> {
                basketDTO.applyDiscount(coupon.getDiscount());
                return basketDTO;
            })
            .or(() -> {
                log.debug("Coupon not applicable or not found for code: {}", code);
                return Optional.of(basketDTO);
            });
    }

    private boolean isCouponApplicable(final BigDecimal basketValue, final BigDecimal discount) {
        if (basketValue == null || discount == null) {
            throw new IllegalArgumentException("Basket value and discount cannot be null");
        }
        
        boolean isApplicable = basketValue.compareTo(BigDecimal.ZERO) > 0 && 
                            basketValue.compareTo(discount) >= 0;
        
        if (!isApplicable) {
            log.debug("Coupon not applicable - basket: {}, discount: {}", basketValue, discount);
        }
        
        return isApplicable;
    }

    //public CouponEntity createCoupon(final CouponRequestDTO couponRequest) {
    public CouponDTO createCoupon(final CouponDTO couponRequestDTO) {
        log.debug("CouponService.createCoupon()");

        if (couponRepository.existsByCode(couponRequestDTO.getCode().toLowerCase())) {
            log.debug("Coupon with code: already exists: {}", couponRequestDTO.getCode().toLowerCase());
            throw new CouponAlreadyExistsException("Coupon with code: already exists!");
        }

        try {
            CouponEntity coupon = couponMapper.toEntity(couponRequestDTO);
            CouponEntity savedCoupon = couponRepository.save(coupon);
            log.debug("Coupon created successfully with ID: {}", savedCoupon.getId());   
            
            return couponMapper.toDTO(savedCoupon);
        } catch (Exception e) {
            // Don't coupon when code is null
            if (e instanceof NullPointerException) {
                log.debug("createCoupon(): Tried to create coupon with null value!");
                throw new RuntimeException("Tried to create coupon with null value!");
            } else {
                log.debug("createCoupon(): Handling other exception: {}",  e.getMessage());
                throw new RuntimeException("Handling other exception!");
            }
        }

        /*CouponEntity coupon = null;

        try {
            coupon = CouponEntity.builder()
                .code(couponRequest.getCode().toLowerCase())
                .discount(couponRequest.getDiscount())
                .minBasketValue(couponRequest.getMinBasketValue())
                .build();

            couponRepository.save(coupon);

        } catch (final NullPointerException e) {
            // Don't coupon when code is null
            log.debug("createCoupon(): Tried to create coupon with null value!");
            throw new RuntimeException("Tried to create coupon with null value!");
        }

        return coupon; */
    }

    public List<CouponDTO> getCoupons(final CouponsRequestDTO couponRequestDTO) {
        log.info("getCoupons()");

        final ArrayList<CouponEntity> foundCoupons = new ArrayList<>();

        couponRequestDTO.getCodes().forEach(code -> 
                foundCoupons.add(getCoupon(code).get()));

        return couponMapper.toDTOList(foundCoupons);
    }
}
