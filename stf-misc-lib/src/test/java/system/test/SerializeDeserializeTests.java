package system.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import com.github.jeansantos38.stf.framework.serialization.DeserializeHelper;
import com.github.jeansantos38.stf.framework.serialization.JsonParserHelper;
import com.github.jeansantos38.stf.framework.serialization.SerializeHelper;
import com.jayway.jsonpath.Option;
import io.qameta.allure.*;
import net.minidev.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;
import system.base.MainTestBase;
import system.pojo.NoteDemoClass;
import system.pojo.Book;

import java.io.File;
import java.lang.String;
import java.util.List;


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

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The serialize helper should work for yaml files")
    @Description("Serialize a yaml object and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void parseJsonReadDotNotationTest() throws Exception {
        String content = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("booksStore.json"));
        String isbn = (String) JsonParserHelper.readJsonPath("$.store.book[3].isbn", content);
        String author = (String) JsonParserHelper.readJsonPath("$.store.book[3].author", content);
        String title = (String) JsonParserHelper.readJsonPath("$.store.book[3].title", content);

        Assert.assertEquals(author, "J. R. R. Tolkien");
        Assert.assertEquals(title, "The Lord of the Rings");
        Assert.assertEquals(isbn, "0-395-19395-8");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("The serialize helper should work for yaml files")
    @Description("Serialize a yaml object and check all its properties values")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void parseJsonReadBracketNotationTest() throws Exception {
        String content = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("booksStore.json"));

        String author = (String) JsonParserHelper.readJsonPath("$['store']['book'][3]['author']", content);
        Assert.assertEquals(author, "J. R. R. Tolkien");

        String notExist = (String) JsonParserHelper.readJsonPath("$['store']['book'][3]['notExist']", content, Option.DEFAULT_PATH_LEAF_TO_NULL);
        Assert.assertNull(notExist);

        List<String> author2 = (List<String>) JsonParserHelper.readJsonPath("$['store']['book'][3]['author']", content, Option.ALWAYS_RETURN_LIST);
        Assert.assertEquals(author2.get(0), "J. R. R. Tolkien");

        // TODO complete this test - WIP
        List<String> book3Object = (List<String>) JsonParserHelper.readJsonPath("$['store']['book'][3]", content, Option.ALWAYS_RETURN_LIST);
        JSONArray authorObjectString = JsonParserHelper.tryReadJsonPath("$['store']['book'][3]['author']", content);
        JSONArray book3rdObject = JsonParserHelper.tryReadJsonPath("$['store']['book'][3]", content);
        String listOfAllBooksObjects = JsonParserHelper.tryParseJsonPathToString("$['store']['book']", content);
        String bookObjectContent = JsonParserHelper.tryParseJsonPathToString("$['store']['book'][3]", content);
        String authorName = JsonParserHelper.tryParseJsonPathToString("$['store']['book'][3]['author']", content);

        Assert.assertEquals(author2.get(0), "J. R. R. Tolkien");

        String title = (String) JsonParserHelper.readJsonPath("$['store']['book'][3]['title']", content);
        String isbn = (String) JsonParserHelper.readJsonPath("$['store']['book'][3]['isbn']", content);

        Assert.assertEquals(title, "The Lord of the Rings");
        Assert.assertEquals(isbn, "0-395-19395-8");
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