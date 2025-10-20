package schwarz.jobs.interview.coupon.services;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import schwarz.jobs.interview.coupon.domain.CouponEntity;
import schwarz.jobs.interview.coupon.repository.CouponRepository;
import schwarz.jobs.interview.coupon.service.CouponService;
import schwarz.jobs.interview.coupon.dto.BasketDTO;
import schwarz.jobs.interview.coupon.dto.CouponDTO;
import schwarz.jobs.interview.coupon.dto.CouponsRequestDTO;
import schwarz.jobs.interview.coupon.mapper.CouponMapper;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private CouponRepository couponRepository;

    private static final String COUPON_CODE = "1111";
    private static final String COUPON_CODE_2 = "12345";
    private static final BigDecimal DISCOUNT = BigDecimal.TEN;
    private static final BigDecimal MIN_BASKET_VALUE = BigDecimal.valueOf(50);

    @Test
    void createCoupon() {

        CouponDTO dto = createCouponDTO(COUPON_CODE_2, DISCOUNT, MIN_BASKET_VALUE);
        CouponEntity expectedEntity = createCouponEntity(COUPON_CODE_2, DISCOUNT, MIN_BASKET_VALUE);
        CouponDTO expectedDTO = createCouponDTO(COUPON_CODE_2, DISCOUNT, MIN_BASKET_VALUE);

        when(couponRepository.existsByCode(COUPON_CODE_2)).thenReturn(Boolean.FALSE);

        when(couponMapper.toEntity(dto)).thenReturn(expectedEntity);  
        when(couponRepository.save(expectedEntity)).thenReturn(expectedEntity); 
        when(couponMapper.toDTO(expectedEntity)).thenReturn(expectedDTO); 

        couponService.createCoupon(dto);

        verify(couponRepository, times(1)).save(any());
    } 

    @Test
    void test_applyBasket_coupon_method() {

        final BasketDTO firstBasket = BasketDTO.builder()
            .value(BigDecimal.valueOf(100))
            .build();

        when(couponRepository.findByCode(COUPON_CODE)).thenReturn(
            Optional.of(createCouponEntity(COUPON_CODE, DISCOUNT, MIN_BASKET_VALUE)));

        Optional<BasketDTO> optionalBasket = couponService.applyBasket(firstBasket, COUPON_CODE);

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b.getValue()).isEqualTo(BigDecimal.valueOf(90));
            assertThat(b.getAppliedDiscount()).isEqualTo(DISCOUNT);
            assertThat(b.isApplicationSuccessful()).isTrue();
        });

        final BasketDTO secondBasket = BasketDTO.builder()
            .value(BigDecimal.valueOf(0))
            .build();

        optionalBasket = couponService.applyBasket(secondBasket, COUPON_CODE);

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b).isEqualTo(secondBasket);
            assertThat(b.isApplicationSuccessful()).isFalse();
        });

        final BasketDTO thirdBasket = BasketDTO.builder()
            .value(BigDecimal.valueOf(-1))
            .build();

        optionalBasket = couponService.applyBasket(thirdBasket, COUPON_CODE);

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b).isEqualTo(thirdBasket);
            assertThat(b.isApplicationSuccessful()).isFalse();
        });

    }

    @Test
    void should_test_get_Coupons() {

        CouponsRequestDTO dto = CouponsRequestDTO.builder()
            .codes(Arrays.asList(COUPON_CODE, COUPON_CODE_2))
            .build();

        CouponEntity coupon1 = createCouponEntity(COUPON_CODE, DISCOUNT, MIN_BASKET_VALUE);
        CouponEntity coupon2 = createCouponEntity(COUPON_CODE_2, DISCOUNT, MIN_BASKET_VALUE);

        CouponDTO expectedCoupon1 = createCouponDTO(COUPON_CODE, DISCOUNT, MIN_BASKET_VALUE);
        CouponDTO expectedCoupon2 = createCouponDTO(COUPON_CODE_2, DISCOUNT, MIN_BASKET_VALUE);                   

        when(couponRepository.findByCode(any()))
            .thenReturn(Optional.of(coupon1))
            .thenReturn(Optional.of(coupon2));

        final ArrayList<CouponEntity> foundCoupons = new ArrayList<>();  
        foundCoupons.add(coupon1);
        foundCoupons.add(coupon2);    
        
        final ArrayList<CouponDTO> expectedCoupons = new ArrayList<>();  
        expectedCoupons.add(expectedCoupon1);
        expectedCoupons.add(expectedCoupon2);

        when(couponMapper.toDTOList(foundCoupons)).thenReturn(expectedCoupons);       

        List<CouponDTO> returnedCoupons = couponService.getCoupons(dto);

        assertThat(returnedCoupons.get(0).getCode()).isEqualTo(COUPON_CODE);
        assertThat(returnedCoupons.get(1).getCode()).isEqualTo(COUPON_CODE_2);
    }

    private CouponEntity createCouponEntity(String code, BigDecimal discount, BigDecimal minBasketValue) {
        return CouponEntity.builder()
                .code(code)
                .discount(discount)
                .minBasketValue(minBasketValue)
                .build();
    }

    private CouponDTO createCouponDTO(String code, BigDecimal discount, BigDecimal minBasketValue) {
        return CouponDTO.builder()
                .code(code)
                .discount(discount)
                .minBasketValue(minBasketValue)
                .build();
    }

    /*@Test
    void shouldThrowExceptionWhenCreatingCouponWithNullDTO() {
        assertThatThrownBy(() -> couponService.createCoupon(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Tried to create coupon with null value!");
    } */
}
