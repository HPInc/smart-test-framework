package hp.inc.jsg.qa.stf.framework.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hp.inc.jsg.qa.stf.enums.serialization.SerializationType;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class DeserializeHelper {

    /***
     * Deserialize a content into a given class. This one use default module for XML.
     * @param tClass: The class type.
     * @param serializationType: Supported serialization types.
     * @param payload: The content to be deserialized.
     * @param <T>: The returned class.
     * @return The deserialized content.
     * @throws Exception
     */
    public static <T> T deserializeStringToObject(Class<T> tClass, SerializationType serializationType, String payload) throws Exception {
        return deserializeStringToObject(tClass, serializationType, null, payload);
    }

    /***
     * Deserialize a content into a given class. This one use configurable module for XML.
     * @param tClass: The class type.
     * @param serializationType: Supported serialization types.
     * @param module: Custom XML wrapper/parser module.
     * @param payload: The content to be deserialized.
     * @param <T>: The returned class.
     * @return The deserialized content.
     * @throws Exception
     */
    public static <T> T deserializeStringToObject(Class<T> tClass, SerializationType serializationType, JacksonXmlModule module, String payload) throws Exception {
        T obj;
        switch (serializationType) {
            case JSON:
                obj = new ObjectMapper().readValue(payload, tClass);
                return obj;
            case XML:
                if (module != null) {
                    obj = new XmlMapper(module).readValue(payload, tClass);
                } else {
                    obj = new XmlMapper().readValue(payload, tClass);
                }
                return obj;
            case YAML:
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                return objectMapper.readValue(payload, tClass);
            default:
                throw new Exception(String.format("The %1$s serialization type is not supported.", serializationType.toString()));
        }
    }
}