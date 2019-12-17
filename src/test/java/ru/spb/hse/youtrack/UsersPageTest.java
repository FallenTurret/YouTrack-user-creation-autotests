package ru.spb.hse.youtrack;

import org.junit.jupiter.api.AfterAll;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    //Positive tests

    @Test
    void userShouldBeAddedSuccessfully() {
        page.createUser("abc", "abc", "abc");
        assertTrue(page.isRegistered("abc"));
        page.deleteUser("abc");
    }

    @Test
    void loginWithNotMoreThenFiftySymbolsShouldBeAdded() {
        String login = new String(new char[50]).replace("\0", "I");
        page.createUser(login, "pass", "pass");
        assertTrue(page.isRegistered(login));
        page.deleteUser(login);
    }

    @Test
    void unicodeSymbolsShouldBeSupportedInAllFields() {
        String login = "U:аыфволд-67,.‣☕\\!?%&#$_";
        page.createUser(login, login, login);
        assertTrue(page.isRegistered(login));
        page.deleteUser(login);
    }

    @Test
    void shouldAllowWhitespacesInPassword() {
        page.createUser("user", " ", " ");
        assertTrue(page.isRegistered("user"));
        page.deleteUser("user");
    }

    @Test
    void shouldAllowProhibitedInLoginSymbolsToUseInPassword() {
        page.createUser("fg", "</>", "</>");
        assertTrue(page.isRegistered("fg"));
        page.deleteUser("fg");
    }

    //Negative tests

    @Test
    void userWithoutLoginShouldNotBeAdded() {
        page.createUser("", "hello", "hello");
        assertFalse(page.isRegistered(""));
    }

    @Test
    void userShouldEnterPassword() {
        page.createUser("user", "", "");
        assertFalse(page.isRegistered("user"));
    }

    @Test
    void userWithoutCorrectPasswordConfirmationShouldNotBeAdded() {
        page.createUser("a1", "password", "ps");
        assertFalse(page.isRegistered("a1"));
    }

    @Test
    void existingLoginShouldNotBeAdded() {
        page.createUser("login", "user1", "user1");
        page.createUser("login", "user2", "user2");
        assertFalse(page.isLoginOccursTwice("login"));
        page.deleteUser("login");
    }

    @Test
    void tooLongLoginShouldNotBeAllowed() {
        String login = new String(new char[51]).replace("\0", "l");
        page.createUser(login, "pass", "pass");
        assertFalse(page.isRegistered(login));
        page.deleteUser(login.substring(0, 50));
    }

    @Test
    void shouldNotAllowWhitespacesInLogin() {
        page.createUser("User name", "password", "password");
        assertFalse(page.isRegistered("User name"));
    }

    @Test
    void shouldNotAllowSpecialSymbolsInLogin() {
        page.createUser("<", "p", "p");
        page.createUser(">", "p", "p");
        page.createUser("/", "p", "p");
        assertFalse(page.isRegistered("<"));
        assertFalse(page.isRegistered(">"));
        assertFalse(page.isRegistered("/"));
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