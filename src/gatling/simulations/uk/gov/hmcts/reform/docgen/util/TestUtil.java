package uk.gov.hmcts.reform.docgen.util;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class TestUtil {

    private final String idamAuth;
    private final String s2sAuth;

    public TestUtil() {
        IdamHelper idamHelper = new IdamHelper(
            Env.getIdamUrl(),
            Env.getOAuthClient(),
            Env.getOAuthSecret(),
            Env.getOAuthRedirect()
        );

        S2sHelper s2sHelper = new S2sHelper(
            Env.getS2sUrl(),
            Env.getS2sSecret(),
            Env.getS2sMicroservice()
        );

        idamAuth = idamHelper.getIdamToken();
        s2sAuth = s2sHelper.getS2sToken();

        RestAssured.useRelaxedHTTPSValidation();
    }

    public RequestSpecification authRequest() {
        return RestAssured
            .given()
            .header("Authorization", idamAuth)
            .header("ServiceAuthorization", s2sAuth);
    }

    public String getIdamAuth() {
        return idamAuth;
    }

    public String getS2sAuth() {
        return s2sAuth;
    }
}
