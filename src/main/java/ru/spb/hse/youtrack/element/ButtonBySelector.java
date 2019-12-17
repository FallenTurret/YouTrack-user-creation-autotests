package ru.spb.hse.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ButtonBySelector extends Clickable {
    private String selector;

    public ButtonBySelector(WebDriver driver, String selector) {
        super(driver);
        this.selector = selector;
    }

    @Override
    protected By locator() {
        return By.cssSelector(selector);
    }
}