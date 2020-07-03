package system.test;

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
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void convertTimestampToDateTest() {
        String extractedDate = convertTimestampToDate(Long.valueOf("1564800583569"));
        Assert.assertEquals(extractedDate, "08/02/2019 23:49:43");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("A QA should be capable of easily handling date formats using STF")
    @Description("Converting a timestamp in specific time format")
    @Link(name = "This could be a Link to your project Issue Tracker", url = "https://github.com/HPInc/smart-test-framework")
    public void convertTimestampUsingPattern() {
        String extractedDate = convertTimestampToDate(Long.valueOf("1564800583569"), "yyyy/dd/MM' 'HH:mm:ss");
        Assert.assertEquals(extractedDate, "2019/02/08 23:49:43");
    }
}