package ru.spb.hse.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ButtonByID extends Clickable {
    private String id;

    public ButtonByID(WebDriver driver, String id) {
        super(driver);
        this.id = id;
    }

    @Override
    protected By locator() {
        return By.id(id);
    }
}