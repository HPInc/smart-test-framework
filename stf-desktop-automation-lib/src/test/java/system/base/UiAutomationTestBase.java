package system.base;

import java.io.File;

public class UiAutomationTestBase extends MainTestBase{

    protected String discoverAbsoluteFilePath(String filename) {
        return new File(String.format("src/test/resources/%s", filename)).getAbsolutePath();
    }
}
