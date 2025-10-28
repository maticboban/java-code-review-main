package schwarz.jobs.interview.coupon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeIssueDTO {

    Long line;
    String severity;
    String message;
    String suggestion;

}
