package com.github.jeansantos38.stf.framework.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class SerializeHelper {

    /***
     * Serialize a Json Object into String.
     * @param jsonObject: The json object
     * @param jsonSerializationInclusion: To define which properties of Java Beans are to be included in serialization.
     * @return The serialized object as string.
     * @throws JsonProcessingException
     */
    public static String serializeJsonObject(Object jsonObject, JsonInclude.Include jsonSerializationInclusion) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (jsonSerializationInclusion != null) {
            mapper.setSerializationInclusion(jsonSerializationInclusion);
        }
        return mapper.writeValueAsString(jsonObject);
    }

    /***
     * Serialize a XML object into String.
     * @param xmlObject: The xml object.
     * @return The serialized object as string.
     * @throws JsonProcessingException
     */
    public static String serializeXmlObject(Object xmlObject) throws JsonProcessingException {
        return serializeXmlObject(xmlObject, null);
    }

    /***
     * Serialize a XML object into String.
     * @param xmlObject: The xml object.
     * @param jacksonXmlModule: The wrapper configuration for this deserialization.
     * @return The serialized object as string.
     * @throws JsonProcessingException
     */
    public static String serializeXmlObject(Object xmlObject, JacksonXmlModule jacksonXmlModule) throws JsonProcessingException {
        XmlMapper xmlMapper;
        if (jacksonXmlModule != null) {
            xmlMapper = new XmlMapper(jacksonXmlModule);
        } else {
            xmlMapper = new XmlMapper();
        }
        return xmlMapper.writeValueAsString(xmlObject);
    }

    /***
     * Serialize a Yaml object into String.
     * @param yamlObject: The yaml object.
     * @return The serialized object as string.
     * @throws JsonProcessingException
     */
    public static String serializeYamlObject(Object yamlObject) throws JsonProcessingException {
        return serializeYamlObject(yamlObject, null, null);
    }

    /***
     * Serialize a Yaml object into String.
     * @param yamlObject: The yaml object.
     * @return The serialized object as string.
     * @throws JsonProcessingException
     */
    public static String serializeYamlObject(Object yamlObject, SerializationFeature serializationFeature, YAMLGenerator.Feature feature) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        if (serializationFeature != null) {
            mapper.disable(serializationFeature);
        }

        if (feature != null) {
            mapper = new ObjectMapper(new YAMLFactory().disable(feature));
        }
        return mapper.writeValueAsString(yamlObject);
    }
}