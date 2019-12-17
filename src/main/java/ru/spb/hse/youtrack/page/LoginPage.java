package ru.spb.hse.youtrack.page;

import org.openqa.selenium.WebDriver;
import ru.spb.hse.youtrack.Settings;
import ru.spb.hse.youtrack.element.ButtonByID;
import ru.spb.hse.youtrack.element.TextField;

public class LoginPage {
    public static final String URL = Settings.URL + "/login";

    private TextField loginField;
    private TextField passwordField;
    private ButtonByID loginButton;

    public LoginPage(WebDriver driver) {
        loginField = new TextField(driver, "l.L.login");
        passwordField = new TextField(driver, "l.L.password");
        loginButton = new ButtonByID(driver,"id_l.L.loginButton");
    }

    public void login(String login, String password) {
        loginField.enterText(login);
        passwordField.enterText(password);
        loginButton.click();
    }
}