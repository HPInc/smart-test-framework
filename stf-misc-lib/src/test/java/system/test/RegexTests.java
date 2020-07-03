package system.test;

import com.github.jeansantos38.stf.framework.regex.RegexHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegexTests {

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Story("A QA should be capable of easily using REGEX on STF")
    @Description("Check if pattern does exist in given text")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void regexFullMatch() throws IOException {
        String guid = "9be48fe8-20a5-4263-b097-d096b2e6309a";
        Assert.assertTrue(RegexHelper.isMatch(RegexHelper.REGEX_GUID, guid));
    }


    @Test(priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Story("A QA should be capable of easily using REGEX on STF")
    @Description("Check if a pattern can be found by Group Matching strategy")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void regexGroupMatch() throws Exception {
        String multipleValues = "cars123home";
        Assert.assertEquals(RegexHelper.returnMatchFromGroup(multipleValues, "((cars))([0-9]{3})([a-z]{4})", 1), "cars");
        Assert.assertEquals(RegexHelper.returnMatchFromGroup(multipleValues, "((cars))([0-9]{3})([a-z]{4})", 3), "123");
        Assert.assertEquals(RegexHelper.returnMatchFromGroup(multipleValues, "((cars))([0-9]{3})([a-z]{4})", 4), "home");
        String modifiedValue = RegexHelper.findReplaceFirstMatch(multipleValues, "[0-9]{3}", "000");
        Assert.assertEquals(modifiedValue, "cars000home");
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Story("A QA should be capable of easily using REGEX on STF")
    @Description("Find and replace a given string by another using REGEX")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void regexFindReplace() throws Exception {
        String multipleValues = "cars123home";
        String modifiedValue = RegexHelper.findReplaceFirstMatch(multipleValues, "[0-9]{3}", "000");
        Assert.assertEquals(modifiedValue, "cars000home");
    }
}