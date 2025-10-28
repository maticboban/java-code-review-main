package schwarz.jobs.interview.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeReviewResponseDTO {

    private CodeIssueDTO[] issues;

    private String summary;

}
