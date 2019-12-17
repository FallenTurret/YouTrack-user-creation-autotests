package ru.spb.hse.youtrack.page;

import org.openqa.selenium.WebDriver;
import ru.spb.hse.youtrack.element.ButtonBySelector;

public class DashboardPage {
    private ButtonBySelector administrationButton;
    private ButtonBySelector usersButton;

    public DashboardPage(WebDriver driver) {
        administrationButton = new ButtonBySelector(driver,
                "#id_l\\.D\\.h\\.header > div.ring-menu > div > div > a:nth-child(3)");
        usersButton = new ButtonBySelector(driver, "body > div.ring-dropdown > div > a:nth-child(2)");
    }

    public void openUsersPage() {
        administrationButton.click();
        usersButton.click();
    }
}