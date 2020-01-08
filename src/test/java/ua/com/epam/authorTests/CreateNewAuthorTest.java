package ua.com.epam.authorTests;

import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;


@Test(description = "Test creating a new author from a random one" +
        "and creating yet another author with same credentials but different authorId")
public class CreateNewAuthorTest extends BaseTest {

    @Test(description = "Post a single Author obj")
    public void postAuthor() {

        Response responseOnCreate = authorService.postAuthor(randomeAuthor);

        // 201 -created
        validatorFactory.authorValidator().
                checkEntityAndCode(responseOnCreate, randomeAuthor, 201);

        // trying to save the same author
        Response responseSameAuthor = authorService.postAuthor(randomeAuthor);

       //  409 - Conflict creating an author with the same id-
        validatorFactory.authorValidator().
                checkCode(responseSameAuthor, 409);

        // changing authorId and saving-
        randomeAuthor.setAuthorId((long) 5000);
        Response responseChangedId = authorService.postAuthor(randomeAuthor);

        validatorFactory.authorValidator().
                checkEntityAndCode(responseChangedId, randomeAuthor, 201);
    }
}
