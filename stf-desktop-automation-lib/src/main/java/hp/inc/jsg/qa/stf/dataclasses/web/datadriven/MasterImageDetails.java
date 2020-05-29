package hp.inc.jsg.qa.stf.dataclasses.web.datadriven;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/

@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterImageDetails {
    public String areaId;
    public Elements[] elements;

    public MasterImageDetails() {
    }
}