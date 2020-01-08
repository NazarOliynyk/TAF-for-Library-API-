package ua.com.epam.authorTests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.com.epam.BaseTest;
import ua.com.epam.entity.Response;

@Test
public class GetAuthorByBookId extends BaseTest {

    @BeforeMethod
    public void sendAuthorsAndGenres() {

        sendAllAuthors();
        sendAllGenres();
    }

    @BeforeMethod
    public void sendBooks() {

        sendAllBooks();
    }

    public void getAuthorByBook(){

        // saving a random book with changed id for incoming author and genre-
        randomBook.setBookId(5000);
        Response responsePostBook = bookService.postSingleBook(randomBook,
                randomeAuthor.getAuthorId(),
                randomGenre.getGenreId());

        // 201 - created
        validatorFactory.authorValidator().
                checkCode(responsePostBook, 201);

        // getting an author by bookId
        Response responseGetAuthor = authorService.
                getAuthorByBookId(randomBook.getBookId());

        //200 -ok
        validatorFactory.authorValidator().
                checkEntityAndCode(responseGetAuthor, randomeAuthor, 200);
    }
}
