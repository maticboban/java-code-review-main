package schwarz.jobs.interview.coupon.constants;

public interface SpringProfile {

    /**
     * profile for local environment
     */
    String LOCAL = "local";

    /**
     * profile for development server (E)
     */
    String DEV = "dev";

    /**
     * NOT profile for development server (E)
     */
    String NOT_DEV = "!dev";

    /**
     * profile for test server (K)
     */
    String TEST = "test";

    /**
     * NOT profile for test server (K)
     */
    String NOT_TEST = "!test";

    /**
     * profile for production server (P)
     */
    String PROD = "prod";

    /**
     * NOT profile for production server (P)
     */
    String NOT_PROD = "!prod";

    /**
     * profile for spring integration tests
     */
    String TESTING = "testing";
    /**
     * NOT profile for spring integration tests
     */
    String NOT_TESTING = "!testing";
}
