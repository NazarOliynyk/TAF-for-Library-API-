package ua.com.epam.authorTests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;

@Test
public class GetAuthorByIdTest extends BaseTest {

    @BeforeMethod
    public void sendAuthors() {

        sendAllAuthors();
    }

    @Test(description = "Get a single Author obj by authorId")
    public void getAuthor() {

        validatorFactory.authorValidator().getAuthor(randomeAuthor);
    }
}
