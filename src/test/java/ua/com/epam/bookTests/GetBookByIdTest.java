package ua.com.epam.bookTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;

@Test
public class GetBookByIdTest extends BaseTest {

    @BeforeMethod
    public void sendAuthorsAndGenres() {

        sendAllAuthors();
        sendAllGenres();
    }

    @BeforeMethod
    public void sendBooks() {

        sendAllBooks();
    }


    @Test(description = "Get a single Book obj ")
    public void getBookById() {

        validatorFactory.bookValidator().getBookById(randomBook);
    }

}
