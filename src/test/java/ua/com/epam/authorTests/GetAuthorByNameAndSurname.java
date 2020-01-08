package ua.com.epam.authorTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;

@Test
public class GetAuthorByNameAndSurname extends BaseTest {

    @BeforeMethod
    public void sendAuthors() {

        sendAllAuthors();
    }

    @Test(description = "Get a single Author obj by name and surname")
    public void getAuthorByInitials() {

        // there are 2 authors with initials containing "Fis"
        Response responseOneCred = authorService.getAuthorListByInitials("Fis");

        validatorFactory.authorValidator().
                checkListSizeAndCode(responseOneCred, 2, 200);


        // there is only one author with initials containing both "Ber" and "Fis"
        Response responseTwoCred = authorService.getAuthorListByInitials("Ber%Fis");

        validatorFactory.authorValidator().
                checkListSizeAndCode(responseTwoCred, 1, 200);

    }
}
