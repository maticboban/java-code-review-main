package schwarz.jobs.interview.coupon.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.dto.CodeReviewRequestDTO;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = CouponController.BASE_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class CodeReviewController {

    public static final String BASE_URL = "/api";

    @PostMapping("/code-review")
    //@CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<?> codeReview(@RequestBody CodeReviewRequestDTO request) {
        // your method implementation
        log.debug("Code review method!");
        return ResponseEntity.ok().build();
    }

}
