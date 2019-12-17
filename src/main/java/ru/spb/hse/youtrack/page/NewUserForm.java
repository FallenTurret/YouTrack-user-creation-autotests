package ru.spb.hse.youtrack.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spb.hse.youtrack.Settings;
import ru.spb.hse.youtrack.element.ButtonByID;
import ru.spb.hse.youtrack.element.ButtonBySelector;
import ru.spb.hse.youtrack.element.TextField;

class NewUserForm {
    private TextField login;
    private TextField password;
    private TextField passwordConfirmation;
    private ButtonByID okButton;
    private ButtonByID cancelButton;
    private ButtonBySelector backToUsers;

    private WebDriver driver;

    NewUserForm(WebDriver driver) {
        this.driver = driver;
        login = new TextField(driver, "l.U.cr.login");
        password = new TextField(driver, "l.U.cr.password");
        passwordConfirmation = new TextField(driver, "l.U.cr.confirmPassword");
        okButton = new ButtonByID(driver, "id_l.U.cr.createUserOk");
        cancelButton = new ButtonByID(driver, "id_l.U.cr.createUserCancel");
        backToUsers = new ButtonBySelector(driver,
                "#id_l\\.E\\.AdminBreadcrumb\\.AdminBreadcrumb > ul > li:nth-child(1) > a");
    }

    void addUser(String login, String password, String passwordConfirmation) {
        this.login.enterText(login);
        this.password.enterText(password);
        this.passwordConfirmation.enterText(passwordConfirmation);
        this.okButton.click();
        WebDriverWait driverWait = new WebDriverWait(driver, 10);
        driverWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.className("error-bulb2")),
                ExpectedConditions.visibilityOfElementLocated(By.className("errorSeverity")),
                ExpectedConditions.not(ExpectedConditions.urlToBe(Settings.URL + "/users"))));
        if (driver.getCurrentUrl().equals(Settings.URL + "/users")) {
            cancelButton.click();
        } else {
            backToUsers.click();
        }
    }
}