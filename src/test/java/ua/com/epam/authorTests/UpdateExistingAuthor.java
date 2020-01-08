package ua.com.epam.authorTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;

@Test
public class UpdateExistingAuthor extends BaseTest {

    @BeforeMethod
    public void sendAuthors() {
        sendAllAuthors();
    }

    @Test(description = "Change description and id of an existing author")
    public void updateAuthor() {

        int newID = 3000;
        String newFN = "Ivan";
        String newSN = "Ivanov";
        Response response = authorService.updateAuthor(randomeAuthor, newID, newFN,newSN);

        validatorFactory.authorValidator().updateAuthor(response, newID, newFN,newSN);
    }
}
