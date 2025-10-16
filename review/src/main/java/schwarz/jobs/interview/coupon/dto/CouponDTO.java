package schwarz.jobs.interview.coupon.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @NotBlank(message = "Code is mandatory")
    @Size(max = 250, message = "Code cannot exceed 250 characters")
    private String code;

    @NotNull(message = "Discount is mandatory")
    private BigDecimal discount;

    private BigDecimal minBasketValue;

}
