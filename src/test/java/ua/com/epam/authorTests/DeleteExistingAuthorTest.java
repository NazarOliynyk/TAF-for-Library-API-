package ua.com.epam.authorTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;

@Test
public class DeleteExistingAuthorTest extends BaseTest {

    @BeforeMethod
    public void sendAuthors() {
        sendAllAuthors();
    }

    @Test(description = "Delete an existing random author")
    public void deleteAuthor() {


        // check if the randomAuthor is present in the DB-
        Response responseBeforeDelete =
                authorService.getAuthor(randomeAuthor.getAuthorId());

        // 200 - ok
        validatorFactory.authorValidator().
                checkEntityAndCode(responseBeforeDelete, randomeAuthor, 200);

        // deleting the random author-
        Response responseOnDeleteAction =
                authorService.deleteAuthor(randomeAuthor.getAuthorId());
        // 204 - no content (response is empty on delete)
        validatorFactory.authorValidator().
                checkCode(responseOnDeleteAction, 204);

       // ascertain that the randomAuthor is not present in the DB-
       // 404 - not found
        Response responseAfterDelete =
                authorService.getAuthor(randomeAuthor.getAuthorId());

        validatorFactory.authorValidator().checkCode(responseAfterDelete, 404);
    }
}
