package system.test;

import com.github.hpinc.jeangiacomin.stf.framework.io.InputOutputHelper;
import com.github.hpinc.jeangiacomin.stf.framework.misc.RandomValuesHelper;
import io.qameta.allure.*;
import system.base.MainTestBase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.io.File;
import java.io.IOException;

public class InputOutputTests extends MainTestBase {

    String fullFilename;
    String content;

    @BeforeClass
    public void beforeClass() {
        testLog.logIt("Running before class steps");
        fullFilename = System.getProperty("user.home") + String.format("/%s.txt", RandomValuesHelper.generateRandomAlphanumeric(6));
        content = "This is the content to be written inside a file";
    }

    @AfterClass
    public void afterClass() throws Exception {
        testLog.logIt("Running After class steps");
        File file = new File(fullFilename);
        if (!file.delete()) {
            throw new Exception("Check cleanup the file was not deleted!");
        }
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be to create a file in hard disk")
    @Description("Create a text file using any string as content")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void createFileOnDisk() throws IOException {
        //Create a text file inside a pre-defined folder with this content below
        try {
            InputOutputHelper.writeFile(content, fullFilename);
        } catch (Exception e) {
            testLog.logIt(e.getMessage());
            Assert.fail("The file was not created!");
        }
    }

    @Test(dependsOnMethods = "createFileOnDisk")
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily reading files from hard disk")
    @Description("Read the file content created by previous test and check if its content matches")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void readCreatedFile() throws IOException {
        String contentFound = InputOutputHelper.readContentFromFile(fullFilename);
        Assert.assertTrue(contentFound.contains(content));
    }
}