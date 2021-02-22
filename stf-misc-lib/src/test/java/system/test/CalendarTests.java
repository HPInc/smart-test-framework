package system.test;

import com.github.jeansantos38.stf.framework.regex.RegexHelper;
import io.qameta.allure.*;
import system.base.MainTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.github.jeansantos38.stf.framework.misc.CalendarHelper.convertTimestampToDate;

public class CalendarTests extends MainTestBase {

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily handling date formats using STF")
    @Description("Converting a timestamp in human readable date format")
    public void convertTimestampToDateTest() {
        String extractedDate = convertTimestampToDate(Long.parseLong("1613569411419"));
        String pattern = "([0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2})";
        Assert.assertTrue(RegexHelper.isMatch(pattern, extractedDate), String.format(
                "Expecting a date in this format: [MM/dd/yyyy HH:mm:ss], but got %s", extractedDate));

    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily handling date formats using STF")
    @Description("Converting a timestamp in specific time format")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void convertTimestampUsingPattern() {
        String extractedDate = convertTimestampToDate(Long.parseLong("1613569411419"), "yyyy/dd/MM' 'HH:mm:ss");
        String pattern = "([0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})";
        Assert.assertTrue(RegexHelper.isMatch(pattern, extractedDate), String.format(
                "Expecting a date in this format: [yyyy/dd/MM HH:mm:ss], but got %s", extractedDate));
    }
}