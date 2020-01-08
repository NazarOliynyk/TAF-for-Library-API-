package ua.com.epam.validators;

import com.google.common.collect.Ordering;
import org.testng.Assert;
import ua.com.epam.entity.Response;
import ua.com.epam.entity.author.Author;
import ua.com.epam.entity.book.Book;
import ua.com.epam.entity.genre.Genre;

import java.util.*;

import static ua.com.epam.utils.JsonKeys.ASCENDING;
import static ua.com.epam.utils.JsonKeys.BOOK_ID;


public class AuthorValidator extends AbstractValidator{

    public void checkEntityAndCode(Response response, Author author, int statusCode) {

        Assert.assertEquals(response.getStatusCode(), statusCode);
        actAuthor = g.fromJson(response.getBody(), Author.class);
        Assert.assertEquals(
                actAuthor,
                author,
                "Actual author's id is not equal to expected. ");
    }

    public void checkCode(Response response, int statusCode){

        softAssert.assertEquals(response.getStatusCode(), statusCode);
    }

    public void updateAuthor(Response response, int newId, String newFN, String newSN) {

        // 200 - OK
        Assert.assertEquals(response.getStatusCode(), 200);

        // compare fields after update-
        actAuthor = g.fromJson(response.getBody(), Author.class);

        softAssert.assertEquals(actAuthor.getAuthorId().intValue(), newId);
        softAssert.assertEquals(actAuthor.getAuthorName().getFirst(), newFN);
        softAssert.assertEquals(actAuthor.getAuthorName().getSecond(), newSN);
        softAssert.assertAll();
    }

    public void checkListSizeAndCode(Response response, int size, int statusCode){
        Assert.assertEquals(response.getStatusCode(), statusCode);
        List<Author> list = g.fromJson(response.getBody(), type);
        Assert.assertEquals(list.size(), size);
    }

    public void getDifferentAuthorsWithOptions(Response response, int size) {

        List<Author> authorList = g.fromJson(response.getBody(), type);
        List<Integer> idList = new ArrayList<>();
        authorList.forEach(author -> idList.add(author.getAuthorId().intValue()));

        // check the size
        softAssert.assertEquals(size, 10);
        // check the order in List
        softAssert.assertTrue(Ordering.natural().reverse().isOrdered(idList));
        softAssert.assertAll();

    }

    public void getAuthorsInGenre(long genreId){

        Response response = authorService.getAuthorsInGenre(genreId);
        // 200 - OK
        Assert.assertEquals(response.getStatusCode(), 200);
        // all authors of certain genre-
        List<Author> authorListByGenre = g.fromJson(response.getBody(), type);
        // map of authors and their books
        Map<Author, List<Book>> booksByGenreMap = new HashMap<>();

        authorListByGenre.forEach(author->
                booksByGenreMap.put(author,
                        g.fromJson(bookService.getBooksByAuthor(
                        author.getAuthorId(),
                        ASCENDING,
                        BOOK_ID).
                        getBody(), typeBook)));

        // List of books of those authors-
        List<Book> booksByGenreList = new ArrayList<>();

        for (Map.Entry<Author, List<Book>> entry : booksByGenreMap.entrySet()) {

                for (Object o : entry.getValue().toArray()) {
                    booksByGenreList.add((Book) o);
                    System.out.println(((Book) o).getBookName());
                }
        }

        // List of genres of those books-
        List<Genre> genres = new ArrayList<>();
        booksByGenreList.forEach( book ->
                genres.add(g.fromJson(genreService.
                        getGenreByBookId(book.getBookId()).getBody(), Genre.class)));

        genres.forEach(genre -> Assert.assertEquals(genre.getGenreId().intValue(), genreId));

    }
}
