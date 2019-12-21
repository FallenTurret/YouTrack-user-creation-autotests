package ru.spb.hse.youtrack;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import ru.spb.hse.youtrack.page.DashboardPage;
import ru.spb.hse.youtrack.page.LoginPage;
import ru.spb.hse.youtrack.page.UsersPage;

import static org.junit.jupiter.api.Assertions.*;

class UsersPageTest {
    private static UsersPage page;
    private static WebDriver driver;

    private static void initDriver() {
        switch (Settings.BROWSER) {
            case "Chrome":
                driver = new ChromeDriver();
                break;
            case "Firefox":
                driver = new FirefoxDriver();
                break;
            case "Edge":
                driver = new EdgeDriver();
                break;
            case "Opera":
                driver = new OperaDriver();
                break;
            case "Safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser \"" + Settings.BROWSER + "\" is not supported");
        }
    }

    @BeforeAll
    static void setUp() {
        initDriver();
        driver.get(LoginPage.URL);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("root", "root");
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.openUsersPage();
        page = new UsersPage(driver);
    }

    private void okTest(String login, String password, String passwordConfirmation) {
        assertEquals("ok", page.createUser(login, password, passwordConfirmation));
        assertTrue(page.isRegistered(login));
        page.deleteUser(login);
    }

    private void errorTest(String login, String password, String passwordConfirmation, String error) {
        assertEquals(error, page.createUser(login, password, passwordConfirmation));
        assertFalse(page.isRegistered(login));
    }

    //Positive tests

    @Test
    void userShouldBeAddedSuccessfully() {
        okTest("abc", "abc", "abc");
    }

    @Test
    void loginWithNotMoreThenFiftySymbolsShouldBeAdded() {
        String login = new String(new char[50]).replace("\0", "I");
        okTest(login, "pass", "pass");
    }

    @Test
    void unicodeSymbolsShouldBeSupportedInAllFields() {
        String login = "U:аыфволд-67,.‣☕\\!?%&#$_";
        okTest(login, login, login);
    }

    @Test
    void shouldAllowWhitespacesInPassword() {
        okTest("user", " ", " ");
    }

    @Test
    void shouldAllowProhibitedInLoginSymbolsToUseInPassword() {
        okTest("fg", "</>", "</>");
    }

    //Negative tests

    @Test
    void userWithoutLoginShouldNotBeAdded() {
        errorTest("", "hello", "hello", "Login is required!");
    }

    @Test
    void userShouldEnterPassword() {
        errorTest("user", "", "", "Password is required!");
    }

    @Test
    void userWithoutCorrectPasswordConfirmationShouldNotBeAdded() {
        errorTest("a1", "password", "ps", "Password doesn't match!");
    }

    @Test
    void existingLoginShouldNotBeAdded() {
        assertEquals("ok", page.createUser("login", "user1", "user1"));
        assertEquals("Value should be unique: login", page.createUser("login", "user2", "user2"));
        assertFalse(page.isLoginOccursTwice("login"));
        page.deleteUser("login");
    }

    @Test
    void tooLongLoginShouldNotBeAllowed() {
        String login = new String(new char[51]).replace("\0", "l");
        assertEquals("ok", page.createUser(login, "pass", "pass"));
        assertFalse(page.isRegistered(login));
        page.deleteUser(login.substring(0, 50));
    }

    @Test
    void shouldNotAllowWhitespacesInLogin() {
        errorTest("User name", "password", "password", "Restricted character ' ' in the name");
    }

    @Test
    void shouldNotAllowSpecialSymbolsInLogin() {
        errorTest("<", "p", "p", "login shouldn't contain characters \"<\", \"/\", \">\": login");
        errorTest(">", "p", "p", "login shouldn't contain characters \"<\", \"/\", \">\": login");
        errorTest("/", "p", "p", "login shouldn't contain characters \"<\", \"/\", \">\": login");
    }

    @AfterEach
    void refresh() {
        driver.navigate().refresh();
    }

    @AfterAll
    static void tearDown() {
        for (String login: page.getLoginNames()) {
            if (!login.equals("root") && !login.equals("guest")) {
                page.deleteUser(login);
            }
        }
        driver.quit();
    }

}