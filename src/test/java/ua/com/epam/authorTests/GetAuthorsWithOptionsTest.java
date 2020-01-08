package ua.com.epam.authorTests;


// Maven dependencies-
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;

import static ua.com.epam.utils.JsonKeys.AUTHOR_ID;
import static ua.com.epam.utils.JsonKeys.DESCENDING;

@Test
public class GetAuthorsWithOptionsTest extends BaseTest {

    @BeforeMethod
    public void sendAuthors() {

        sendAllAuthors();
    }

    @Test(description = "Get first 10 authors sorted by authorId in descending order")
    public void getDifferentAuthorsWithOptions() {

        Response response =
                authorService.getAuthorListByParams(
                        DESCENDING,
                        1,
                        true,
                        10,
                        AUTHOR_ID);
        // 200 -ok
        validatorFactory.authorValidator().
                checkCode(response, 200);

        validatorFactory.
                authorValidator().
                getDifferentAuthorsWithOptions(response,10);
    }

}
