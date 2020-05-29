package hp.inc.jsg.qa.stf.framework.datadriven;

import hp.inc.jsg.qa.stf.dataclasses.datadriven.Statement;
import hp.inc.jsg.qa.stf.dataclasses.web.datadriven.Element;
import hp.inc.jsg.qa.stf.framework.desktop.DesktopAutomationHelper;
import hp.inc.jsg.qa.stf.framework.logger.TestLog;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;


/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class SetDesktopAutomationInstructionSet {

    private TestLog testLog;
    private DesktopAutomationHelper desktopAutomationHelper;

    /***
     * Default constructor.
     * @param testLog: LoggerQA class instance.
     * @param desktopAutomationHelper: Image Recognition Helper instance.
     */
    public SetDesktopAutomationInstructionSet(TestLog testLog, DesktopAutomationHelper desktopAutomationHelper) {
        this.testLog = testLog;
        this.desktopAutomationHelper = desktopAutomationHelper;
    }

    /***
     * This helper does all the magic required for setting an image recognition data driven.
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @param statement: Data driven statement object.
     * @throws Exception
     */
    public void setImageDataDriven(String navigatorFileFullPath, Statement statement) throws Exception {
        Element mElement;
        Pattern mPattern = null;

        //This try catch block is required to prevent hard times while triage a null pointer exceptions due to typos in statement selectorId.
        try {
            mElement = this.desktopAutomationHelper.retrieveTargetElement(navigatorFileFullPath, statement.areaId, statement.selector).element;
        } catch (Exception e) {
            this.testLog.logIt(e.getMessage());
            throw new Exception(String.format("The element\\selectorId [%1$s] provided by data driven statement does not exist in the given area [%2$s] and navigator file [%3$s]. Please fix that and run again the test.", statement.selector, statement.areaId, navigatorFileFullPath));
        }

        try {
            mPattern = this.desktopAutomationHelper.retrievePatternFromNavigator(navigatorFileFullPath, statement.areaId, statement.selector);
        } catch (Exception e) {
            this.testLog.logIt(e.getMessage());
        }

        String mCtrlType = mElement == null || mElement.ctrlType == null ? "UNKNOWN-NOT PROVIDED IN NAVIGATOR" : mElement.ctrlType;
        this.testLog.logIt(String.format("[Action:%1$s][ContentType:%2$s][ControlType:%3$s][Selector:%4$s]", statement.action, statement.contentType, mCtrlType, mElement.selector));

        switch (statement.action) {
            case "setField":
                switch (statement.method) {
                    case "byPaste":
                        performSetTextField(mElement, mPattern, statement, statement.action);
                        break;

                    case "byKeystrokeSequence":
                        performKeyStroke(mElement, mPattern, statement, mCtrlType);
                        break;

                    case "clickSequence":
                        performClickSequence(statement, navigatorFileFullPath);
                        break;

                    case "byPointClick":
                        if (statement.contentType == null || statement.contentType.isEmpty()) {
                            mPattern.targetOffset(mElement.xCoordinate, mElement.yCoordinate);
                        } else {
                            mPattern.targetOffset(statement.xCoordinate, statement.yCoordinate);
                        }
                        performClick(mElement, mPattern);
                        break;

                    case "byEvaluatePointClick":
                        if (statement.contentType.equals("boolean")) {
                    /*
                    This is one should be used only by checkboxes and radio buttons. The idea is simple.
                    The navigator should have only an entry for this element enabled (checked) and the statement element selector will point to that nav. but passing its own region coordinates, then this method will check for the existence of this pattern.
                    If exists and the content value is true - nothing to do here. Otherwise....
                     */
                            if (!mElement.ctrlType.equals("checkBox") && !mElement.ctrlType.equals("radioButton")) {
                                throw new Exception(String.format("The action [%1$s] combined with content type [%2$s] - could be only used by Checkboxes or Radio buttons - instead found [%3$s].", statement.action, statement.contentType, mElement.ctrlType));
                            }

                            boolean statementValue = (Boolean) statement.content[0];
                            //Assemble the reference pattern that will be used to create the region and later to perform a hover click.
                            Pattern refPattern = this.desktopAutomationHelper.retrievePatternFromNavigator(navigatorFileFullPath, statement.areaId, statement.selector, false);
                            //Create the region by reading its coordinates from statement content.
                            Region region = this.desktopAutomationHelper.createRegionFromReferencePattern(refPattern, statement.rectangleTopLeftX, statement.rectangleTopLeftY, statement.rectangleBottomRightX, statement.rectangleBottomRightY);
                            //Check if the master exists inside the given region.
                            boolean masterExists = this.desktopAutomationHelper.patternExistsInRegion(region, mPattern, this.desktopAutomationHelper.getTakeRegionScreenshotIfPatternNotFound(), 2);

                            //Get the offset.
                            refPattern.targetOffset(statement.xCoordinate, statement.yCoordinate);

                            if (statementValue && !masterExists) {
                                this.testLog.logIt("Currently disabled, enabling it!");
                                this.desktopAutomationHelper.hoverClick(refPattern);
                            } else if (!statementValue && masterExists) {
                                this.testLog.logIt("Currently enable, disabling it!");
                                this.desktopAutomationHelper.hoverClick(refPattern);
                            } else {
                                this.testLog.logIt(String.format("Already %1$s, nothing to do here.", statementValue ? "enabled" : "disabled"));
                            }
                        } else {
                            throw new Exception(String.format("The content type [%1$s] isn't supported by action [%2$s]", statement.contentType, statement.action));
                        }
                        break;

                    default:
                        throw new Exception(String.format("The method:[%1$s] is not supported!!!", statement.method));
                }
                break;

            default:
                throw new Exception(String.format("The action type:[%1$s] is not supported!!!", statement.action));
        }
    }

    /***
     * Helper to set a text field by pasting or typing.
     * @param element
     * @param pattern: An object from a master image file.
     * @param statement: Data driven statement object.
     * @throws Exception
     */
    private void performSetTextField(Element element, Pattern pattern, Statement statement, String action) throws Exception {
        if (element.interactionType.equals("indirect")) {
            this.desktopAutomationHelper.hoverDoubleClick(pattern);
        } else {
            this.desktopAutomationHelper.doubleClick(pattern);
        }

        if (action.equals("setField_byPaste")) {
            this.desktopAutomationHelper.getScreen().paste((String) statement.content[0]);
        } else {
            this.desktopAutomationHelper.getScreen().type((String) statement.content[0]);
        }
    }

    /***
     * Simple helper that performs a keystroke sequence.
     * @param element
     * @param pattern: An object from a master image file.
     * @param statement: Data driven statement object.
     * @param ctrlType
     * @throws Exception
     */
    private void performKeyStroke(Element element, Pattern pattern, Statement statement, String ctrlType) throws Exception {
        //In case of DDL, then click on it in order to show(expand) its content.
        if (ctrlType.equals("dropDownList")) {
            if (element.interactionType.equals("indirect")) {
                this.desktopAutomationHelper.hoverClick(pattern);
            } else {
                this.desktopAutomationHelper.click(pattern);
            }
        }
        //Start the keystroke sequence.
        for (int j = 0; j < statement.content.length; j++) {
            this.desktopAutomationHelper.getScreen().type((String) statement.content[j]);
        }
    }

    /***
     * Simple helper that performs a click.
     * @param element
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    private void performClick(Element element, Pattern pattern) throws Exception {
        performClick(element, null, pattern);
    }

    /***
     * Simple helper that performs a click.
     * @param element
     * @param pattern: An object from a master image file.
     * @throws Exception
     */
    private void performClick(Element element, Region region, Pattern pattern) throws Exception {

        //TODO FIND A WAY TO KNOW IF THE TARGET IS ALREADY SELECTED OR NOT, IN ORDER TO PERFORM THE CORRECT ACTION. TODAY IT'S JUST CLICKING. - See more details in the comments below.
        /*
        I believe the less worst way of doing this is on two steps. First we'll need to have a unique screenshot of the element we want to test (it could be a check box and a partial of its label) and save it with the same name of the selector plus _checked (instead of using the master pattern) and keep it out of the records.
        e.g.: "selector": "cbx_allowInsecureComm",  -> cbx_allowInsecureComm_checked.png

        So it should work this way, It will load everything as usual, but when reach this action related to select (or not) an element, then since we'll now that adding _checked.png we can create a pattern; Using this pattern with Exists, we would know if it's selected or not, then we could leave it flow according the content provided (true or false).
         */
        if (element.interactionType.equals("indirect")) {
            if (region == null) {
                this.desktopAutomationHelper.hoverClick(pattern);
            } else {
                this.desktopAutomationHelper.hoverClick(region, pattern);
            }
        } else {
            if (region == null) {
                this.desktopAutomationHelper.click(pattern);
            } else {
                this.desktopAutomationHelper.click(region, pattern);
            }
        }
    }

    /***
     * Simple helper that performs click(s) sequence.
     * @param statement: Data driven statement object.
     * @param navigatorFileFullPath: The file that contains a navigator content.
     * @throws Exception
     */
    private void performClickSequence(Statement statement, String navigatorFileFullPath) throws Exception {
        for (int j = 0; j < statement.content.length; j++) {
            String tempSelector = (String) statement.content[j];
            Element tempElement = this.desktopAutomationHelper.retrieveTargetElement(navigatorFileFullPath, statement.areaId, tempSelector).element;
            Pattern tempPattern = this.desktopAutomationHelper.retrievePatternFromNavigator(navigatorFileFullPath, statement.areaId, tempSelector);
            if (tempElement.interactionType.equals("indirect")) {
                this.desktopAutomationHelper.hoverClick(tempPattern);
            } else {
                this.desktopAutomationHelper.click(tempPattern);
            }
        }
    }
}