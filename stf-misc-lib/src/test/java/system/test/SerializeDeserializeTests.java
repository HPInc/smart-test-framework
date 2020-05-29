package system.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import hp.inc.jsg.qa.stf.enums.serialization.SerializationType;
import hp.inc.jsg.qa.stf.framework.io.InputOutputHelper;
import hp.inc.jsg.qa.stf.framework.serialization.DeserializeHelper;
import hp.inc.jsg.qa.stf.framework.serialization.SerializeHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import system.base.MainTestBase;
import system.pojo.NoteDemoClass;

import java.io.File;


public class SerializeDeserializeTests extends MainTestBase {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The deserialize helper should work for json files")
    @Description("Deserialize a json file and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void deserializeJsonContent() throws Exception {

        String content = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("content.json"));
        NoteDemoClass noteDemoClass = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.JSON, content);

        assertAllPropertiesValuesFromNoteObject(noteDemoClass);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The deserialize helper should work for xml files")
    @Description("Deserialize a xml file and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void deserializeXmlContent() throws Exception {
        String content = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("content.xml"));
        NoteDemoClass noteDemoClass = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.XML, content);

        assertAllPropertiesValuesFromNoteObject(noteDemoClass);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The deserialize helper should work for yaml files")
    @Description("Deserialize a yaml file and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void deserializeYamlContent() throws Exception {
        String content = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("content.yml"));
        NoteDemoClass noteDemoClass = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.YAML, content);

        assertAllPropertiesValuesFromNoteObject(noteDemoClass);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The serialize helper should work for json files")
    @Description("Serialize a json object and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void serializeJsonContent() throws Exception {
        NoteDemoClass noteDemoClass = new NoteDemoClass().factory();
        String serializedString = SerializeHelper.serializeJsonObject(noteDemoClass, JsonInclude.Include.NON_NULL);
        NoteDemoClass deserializedDemoClass = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.JSON, serializedString);

        compareSerializedAndDeserializedObjects(deserializedDemoClass, noteDemoClass);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The serialize helper should work for xml files")
    @Description("Serialize a xml object and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void serializeXmlContent() throws Exception {
        NoteDemoClass noteDemoClass = new NoteDemoClass().factory();
        String serializedString = SerializeHelper.serializeXmlObject(noteDemoClass, null);

        NoteDemoClass deserializeStringToObject = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.XML, serializedString);

        compareSerializedAndDeserializedObjects(deserializeStringToObject, noteDemoClass);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The serialize helper should work for yaml files")
    @Description("Serialize a yaml object and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void serializeYamlContent() throws Exception {
        NoteDemoClass noteDemoClass = new NoteDemoClass().factory();
        String serializedString = SerializeHelper.serializeYamlObject(noteDemoClass);

        NoteDemoClass deserializedDemoClass = DeserializeHelper.deserializeStringToObject(NoteDemoClass.class, SerializationType.YAML, serializedString);

        compareSerializedAndDeserializedObjects(deserializedDemoClass, noteDemoClass);
    }

    private void assertAllPropertiesValuesFromNoteObject(NoteDemoClass noteDemoClass) {
        Assert.assertEquals(noteDemoClass.note.heading, "Urgent!");
        Assert.assertEquals(noteDemoClass.note.from, "STF");
        Assert.assertEquals(noteDemoClass.note.to, "QA User");
        Assert.assertEquals(noteDemoClass.note.body, "Don't forget that STF means Smart Test Framework");
    }

    private void compareSerializedAndDeserializedObjects(NoteDemoClass currentValue, NoteDemoClass expectedValue) {
        Assert.assertEquals(currentValue.note.heading, expectedValue.note.heading);
        Assert.assertEquals(currentValue.note.from, expectedValue.note.from);
        Assert.assertEquals(currentValue.note.to, expectedValue.note.to);
        Assert.assertEquals(currentValue.note.body, expectedValue.note.body);
    }

    private String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }
}