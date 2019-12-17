package ru.spb.hse.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Clickable {
    private WebDriver driver;
    private WebDriverWait driverWait;

    Clickable(WebDriver driver) {
        this.driver = driver;
        driverWait = new WebDriverWait(driver, 10);
    }

    public void click() {
        driverWait.until(ExpectedConditions.elementToBeClickable(locator()));
        driver.findElement(locator()).click();
    }

    abstract protected By locator();
}