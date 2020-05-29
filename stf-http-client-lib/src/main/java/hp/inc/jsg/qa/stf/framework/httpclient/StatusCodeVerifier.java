package hp.inc.jsg.qa.stf.framework.httpclient;

import hp.inc.jsg.qa.stf.enums.http.StatusCodeVerifierStrategy;
import hp.inc.jsg.qa.stf.framework.regex.RegexHelper;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

public class StatusCodeVerifier {

    int expectedStatusCode;
    int[] expectedStatusCodes;
    String expectedStatusCodeRegex;
    StatusCodeVerifierStrategy statusCodeVerifierStrategy;

    public StatusCodeVerifier(int expectedStatusCode) throws Exception {
        verifyStatusCodesCompliance(new int[]{expectedStatusCode});
        this.expectedStatusCode = expectedStatusCode;
        this.statusCodeVerifierStrategy = StatusCodeVerifierStrategy.EXPECTED_STATUS_CODE;
    }

    public StatusCodeVerifier(int[] expectedStatusCodes) throws Exception {
        verifyStatusCodesCompliance(expectedStatusCodes);
        this.expectedStatusCodes = expectedStatusCodes;
        this.statusCodeVerifierStrategy = StatusCodeVerifierStrategy.LIST_OF_POSSIBLE_STATUS_CODES;
    }

    public StatusCodeVerifier(String regexForPossibleStatusCodes) throws Exception {
        if (regexForPossibleStatusCodes == null || regexForPossibleStatusCodes.isEmpty()) {
            throw new Exception("The regex must be filled");
        }
        this.expectedStatusCodeRegex = regexForPossibleStatusCodes;
        this.statusCodeVerifierStrategy = StatusCodeVerifierStrategy.REGEX_FOR_POSSIBLE_STATUS_CODES;
    }

    public void verifyStatusCode(int httpStatusCode) throws Exception {
        switch (this.statusCodeVerifierStrategy) {
            case EXPECTED_STATUS_CODE:
                if (this.expectedStatusCode != httpStatusCode) {
                    throw new Exception(String.format("The status code %s didn't match: %s", httpStatusCode, this.expectedStatusCode));
                }
                break;
            case LIST_OF_POSSIBLE_STATUS_CODES:
                boolean hasMatch = false;
                for (int statusCode : this.expectedStatusCodes) {
                    if (statusCode == httpStatusCode) {
                        hasMatch = true;
                        break;
                    }
                }
                if (!hasMatch) {
                    throw new Exception(String.format("The status code %s didn't match: %s", httpStatusCode, this.expectedStatusCodes));
                }
                break;
            case REGEX_FOR_POSSIBLE_STATUS_CODES:
                if (!RegexHelper.isMatch(this.expectedStatusCodeRegex, Integer.toString(httpStatusCode))) {
                    throw new Exception(String.format("The status code %s didn't match the provided regex: %s", httpStatusCode, this.expectedStatusCodeRegex));
                }
                break;
            default:
                throw new Exception(String.format("The status code strategy is not supported!", this.statusCodeVerifierStrategy.toString()));
        }
    }

    private void verifyStatusCodesCompliance(int[] possibleStatusCodes) throws Exception {
        if (possibleStatusCodes == null || possibleStatusCodes.length == 0) {
            throw new Exception("You must provide what is\\are the expected status code(s)!!!");
        }

        for (int statusCode : possibleStatusCodes) {
            String statusTemp = Integer.toString(statusCode);
            if (!RegexHelper.isMatch("[1-5][0-9][0-9]", statusTemp)) {
                throw new Exception(String.format("The status code %s does not match the possible HTTP status code spec!!!", statusTemp));
            }
        }
    }
}