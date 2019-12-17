package ru.spb.hse.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TextField {
    private String name;

    private WebDriver driver;
    private WebDriverWait driverWait;

    public TextField(WebDriver driver, String name) {
        this.driver = driver;
        this.driverWait = new WebDriverWait(driver, 10);
        this.name = name;
    }

    public void enterText(String text) {
        driverWait.until(ExpectedConditions.elementToBeClickable(By.name(name)));
        driver.findElement(By.name(name)).sendKeys(text);
    }
}