package hp.inc.jsg.qa.stf.dataclasses.web.http;

import com.google.common.base.Stopwatch;
import org.apache.http.HttpResponse;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class HttpInternalResponse {

    public Stopwatch stopwatch;
    public HttpResponse httpResponse;

    public HttpInternalResponse(Stopwatch stopwatch, HttpResponse httpResponse) {
        this.stopwatch = stopwatch;
        this.httpResponse = httpResponse;
    }
}