package schwarz.jobs.interview.coupon.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.service.CouponService;
import schwarz.jobs.interview.coupon.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.dto.BasketDTO;
import schwarz.jobs.interview.coupon.dto.CouponDTO;
import schwarz.jobs.interview.coupon.dto.CouponsRequestDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@PreAuthorize("isAuthenticated()")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = CouponController.BASE_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Coupon Management", description = "APIs for managing coupons")
public class CouponController {
    public static final String BASE_URL = "/v1/coupon-service";

    private final CouponService couponService;

    //@ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket - Version 1")
    //@PreAuthorize("hasAnyAuthority()")
    @PostMapping(value = "/basket/apply")
    public ResponseEntity<BasketDTO> applyBasket(
        //@ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
        // request is automatically validated before this method is executed
        // Validation happens automatically with @Valid
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {
        log.debug("CouponController.applyBasket(): Received request to apply coupon.");

        if (Objects.isNull(applicationRequestDTO)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application request was null!");
        }

        final Optional<BasketDTO> basketDTO =
            couponService.applyBasket(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (basketDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.debug("Applied coupon for basket: {}", applicationRequestDTO.getBasket());
        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }

    //@PreAuthorize("hasAnyAuthority()")
    @PostMapping(value = "/create")
    public ResponseEntity<Void> create(@RequestBody @Valid final CouponDTO couponRequestDTO) {
        log.debug("CouponController.create()");

        if (Objects.isNull(couponRequestDTO)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon request was null!");
        }

        //final CouponEntity coupon = couponService.createCoupon(couponRequestDTO);
        final CouponDTO createdCoupon = couponService.createCoupon(couponRequestDTO);

        return ResponseEntity.ok().build();
        //return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    //@PreAuthorize("hasAnyAuthority()")
    @GetMapping(value = "/coupons")
    public ResponseEntity<List<CouponDTO>> getCoupons(@RequestBody @Valid final CouponsRequestDTO couponRequestDTO) {
        log.debug("CouponController.getCoupons()");

        if (Objects.isNull(couponRequestDTO)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupons request was null!");
        }

        List<CouponDTO> couponsList = couponService.getCoupons(couponRequestDTO);

        return ResponseEntity.ok().body(couponsList);
    }
}
