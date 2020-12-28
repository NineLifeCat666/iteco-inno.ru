import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class ItekoTests extends TestBase{

    Faker faker = new Faker();
    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-Gb"), new RandomService());
    String name = faker.name().firstName();
    String surname = faker.name().lastName();
    String email = fakeValuesService.bothify("???##@gmail.com");
    String telephoneNumber = fakeValuesService.regexify("[0-9]{10}");
    File file = new File("src/test/resources/test.pdf");

    String itekoSite = "https://iteco-inno.ru";

    String vacancy = "Automation engineer",
           messageText = "test";

    @Test
    @DisplayName("Поиск элементов на главной странице сайта")
    void searchElementsOnMainPageTest() {
        step("Открываем главную страницу", () -> {
            open(itekoSite);
        });

        step("Проверяем наличие лозунга - Реальный опыт в достижении результата", () -> {
            $("[class='vcontainer']").shouldHave(text("Реальный опыт в достижении результата"));
        });

        step("Проверяем наличие элемента Направления деятельности на странице", () -> {
            $("[class='frame-container']").shouldHave(text("Направления деятельности"));
        });
    }

    @Test
    @DisplayName("Навигация по меню")
    void navigationTest() {
        step("Открываем главную страницу", () -> {
            open(itekoSite);
        });

        step("Ищем вкладку  Обеспечение качества и тестирования в drop-down menu", () -> {
            $("#nav-item-84").pressEnter();
            $("[class='dropdown-menu show']").shouldHave(text("Обеспечение качества и тестирование"));
        });

        step("переходим на старницу Обеспечение качества и тестирование", () -> {
            $("[class='dropdown-menu show']").find(byTitle("Обеспечение качества и тестирование")).click();
            $("#c382").shouldHave(text("Автоматизация тестирования:"));
        });
    }

    @Test
    @Disabled("Тест игнорируется, что-бы не спамить заполненными резюме на проде")
    @DisplayName("Успешное заполение и отправка данных  в форме для резюме")
    void successfulFillResumeFormTest() {
        step("открываем страницу с заполнением резюме", () -> {
            open("https://iteco-inno.ru/resume");
        });

        step("успешное заполнение формы", () -> {
            $("#jobForm-295-name").val(name);
            $("#jobForm-295-text-1").val(surname);
            $("#jobForm-295-email").val(email);
            $("#jobForm-295-telephone-1").val(telephoneNumber);
            $("#jobForm-295-subject").val(vacancy);
            $("#jobForm-295-message").val(messageText);
            $("#jobForm-295-fileupload-1").uploadFile(file);
            $(".custom-control-label").click();
            $(".btn-primary").click();
            $("#c296").shouldHave(text("Ваше резюме отправлено. Спасибо!"));
        });
    }
}
