package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppError;
import jdk.jfr.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {


    @Description("checking login page title ----")
   // @Severity(SeverityLevel.MINOR)
  //  @Owner("Naveen Automation Labs")
   // @Issue("Login-123")
    @Test(priority = 1)
    public void loginPageTitleTest() {
        String actTitle = loginPage.getLoginPageTitle();
        Assert.assertEquals(actTitle, AppConstants.LOGIN_PAGE_TITLE, AppError.TITLE_NOT_FOUND);
    }

    @Description("checking login page url ----")
 //   @Severity(SeverityLevel.NORMAL)
   // @Owner("Naveen Automation Labs")
    @Test(priority = 2)
    public void loginPageURLTest() {
        String actURL = loginPage.getLoginPageURL();
        Assert.assertTrue(actURL.contains(AppConstants.LOGIN_PAGE_FRACTION_URL), AppError.URL_NOT_FOUND);
    }

    @Description("checking forgot pwd link exist on the login page ----")
  //  @Severity(SeverityLevel.CRITICAL)
  //  @Owner("Naveen Automation Labs")
    @Test(priority = 3)
    public void forgotPwdLinkExistTest() {
        Assert.assertTrue(loginPage.checkForgotPwdLinkExist(), AppError.ELEMENT_NOT_FOUND);
    }

    @Description("checking user is able to login successfully ----")
  //  @Severity(SeverityLevel.BLOCKER)
   // @Owner("Naveen Automation Labs")
    @Test(priority = 4)
    public void loginTest() {
        accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
        Assert.assertEquals(accPage.getAccPageTitle(), AppConstants.ACCOUNTS_PAGE_TITLE, AppError.TITLE_NOT_FOUND);
    }




}
