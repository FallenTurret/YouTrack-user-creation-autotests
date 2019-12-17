package ru.spb.hse.youtrack.page;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spb.hse.youtrack.element.ButtonByID;

import java.util.List;
import java.util.stream.Collectors;

public class UsersPage {
    private ButtonByID createUserButton;
    private NewUserForm form;
    private LoginList loginNames;

    private WebDriver driver;

    public UsersPage(WebDriver driver) {
        this.driver = driver;
        createUserButton = new ButtonByID(driver, "id_l.U.createNewUser");
        form = new NewUserForm(driver);
        loginNames = new LoginList(driver);
    }

    public void createUser(String login, String password, String passwordConfirmation) {
        createUserButton.click();
        form.addUser(login, password, passwordConfirmation);

        waitTillValid();
    }

    public void deleteUser(String login) {
        loginNames.deleteUser(login);

        waitTillValid();
    }

    public List<String> getLoginNames() {
        return loginNames.getLoginList();
    }

    public boolean isRegistered(String login) {
        return getLoginNames().contains(login);
    }

    public boolean isLoginOccursTwice(String login) {
        return getLoginNames().stream().filter(login::equals).count() >= 2;
    }

    private void waitTillValid() {
        WebDriverWait driverWait = new WebDriverWait(driver, 1);
        while (true) {
            try {
                driverWait.until(ExpectedConditions.elementToBeClickable(By.id("id_l.U.createNewUser")));
                break;
            } catch (StaleElementReferenceException ignored) {
            }
        }
    }

    private static class LoginList {
        private static final String XPATH = "//*[starts-with(@id, 'id_l.U.usersList.UserLogin.editUser')]";

        private WebDriver driver;
        private WebDriverWait driverWait;

        private LoginList(WebDriver driver) {
            this.driver = driver;
            driverWait = new WebDriverWait(driver, 10);
        }

        private void waitToBeAbleToGet() {
            driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH)));
        }

        private List<String> getLoginList() {
            waitToBeAbleToGet();
            return driver.findElements(By.xpath(XPATH))
                    .stream().map(WebElement::getText).collect(Collectors.toList());
        }

        private void deleteUser(String login) {
            waitToBeAbleToGet();
            List<WebElement> users = driver.findElements(By.xpath(XPATH)).stream()
                    .filter(element -> element.getText().equals(login)).collect(Collectors.toList());
            if (users.size() != 1)
                return;

            WebElement user = users.get(0);
            user.findElement(By.xpath(".//../../td[6]/a[1]")).click();
            driver.switchTo().alert().accept();
            driver.switchTo().defaultContent();
        }
    }
}