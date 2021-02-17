package com.github.jeansantos38.stf.framework.serialization;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import net.minidev.json.JSONArray;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class JsonParserHelper {

    /**
     * Use regular cast to get the values.
     * @param jsonPathExpression
     * @param jsonDocument
     * @param options
     * @return can be (String), (List<String>) or (JSONArray) - it will depends on what you're trying to get.
     */
    public static Object readJsonPath(String jsonPathExpression, String jsonDocument, Option... options) {
        return JsonPath.using(setConfig(options)).parse(jsonDocument).read(jsonPathExpression);
    }

    /**
     * Return any match as object so it's possible to interact with it and all its attributes.
     * It works in a best effort basis, even some errors in path expressions might be suppressed.
     * @param jsonPathExpression
     * @param jsonDocument
     * @return
     */

    public static JSONArray tryReadJsonPath(String jsonPathExpression, String jsonDocument) {
        return JsonPath.using(setConfig(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS)).parse(jsonDocument).read(jsonPathExpression);
    }

    /**
     * It will return an attribute value or convert an entire json object to plain text.
     * It works grate for getting an attribute value. For a path that returns an object it might return a lot of information
     * accordingly the size of json object.
     * @param jsonPathExpression
     * @param jsonToBeParsed
     * @return
     */
    public static String tryParseJsonPathToString(String jsonPathExpression, String jsonToBeParsed) {
        JSONArray match = JsonPath.using(setConfig(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS)).parse(jsonToBeParsed).read(jsonPathExpression);
        return match.get(0).toString();
    }

    private static Configuration setConfig(Option... options) {
        Configuration configuration = Configuration.defaultConfiguration();
        return configuration.addOptions(options);
    }
}