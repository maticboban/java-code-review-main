package schwarz.jobs.interview.coupon.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketDTO {

    @NotNull
    private BigDecimal value;

    private BigDecimal appliedDiscount;

    @Builder.Default
    private boolean applicationSuccessful = false;

    public void applyDiscount(final BigDecimal discount) {
        this.applicationSuccessful = true;
        this.appliedDiscount = discount;
        this.value = this.value.subtract(discount);
    }

}
