package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationByCustomerInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.generateByInfo;

public class AllureTest {

    RegistrationByCustomerInfo registrationInfo = generateByInfo("ru");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldEnterValidData() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Калуга");
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getFullName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text((DataGenerator.forwardDate(3))));
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(5));
        $(byText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15));
        $(byText("Перепланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(text((DataGenerator.forwardDate(5))));
    }

    @Test
    void shouldGetErrorIfInvalidCity() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Караганда");
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getFullName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(".input_invalid[data-test-id='city']").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldGetErrorIfEmptyCity() {
        open("http://localhost:9999");
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getFullName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(".input_invalid[data-test-id='city']").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorIfInvalidName() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Калуга");
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id='name'] input").setValue("Petrov");
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhoneNumber());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(".input_invalid[data-test-id='name']").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldGetErrorIfNotClickAgreement() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Калуга");
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getFullName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhoneNumber());
        $(byText("Запланировать")).click();
        $(".input_invalid[data-test-id='agreement']").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}