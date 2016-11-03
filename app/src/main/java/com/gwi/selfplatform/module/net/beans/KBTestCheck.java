package com.gwi.selfplatform.module.net.beans;

public class KBTestCheck extends KBBase {
    private long TestId;
    private String TestName;
    private String TestKind;
    public long getTestId() {
        return TestId;
    }
    public void setTestId(long testId) {
        TestId = testId;
    }
    
    public String getTestName() {
        return TestName;
    }
    public void setTestName(String testName) {
        TestName = testName;
    }
    public String getTestKind() {
        return TestKind;
    }
    public void setTestKind(String testKind) {
        TestKind = testKind;
    }
    
}
