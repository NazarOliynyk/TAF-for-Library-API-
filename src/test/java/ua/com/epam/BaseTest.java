package ua.com.epam;

//klov-server imports  ----
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.KlovReporter;
import org.testng.ITestResult;
//-------------------

import ua.com.epam.service.AuthorService;
import ua.com.epam.service.BookService;
import ua.com.epam.service.GenreService;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ua.com.epam.core.rest.RestClient;
import ua.com.epam.entity.author.Author;
import ua.com.epam.entity.book.Book;
import ua.com.epam.entity.genre.Genre;
import ua.com.epam.service.CleanUpService;
import ua.com.epam.utils.DataFactory;
import ua.com.epam.validators.ValidatorFactory;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import static ua.com.epam.config.URI.POST_AUTHOR_SINGLE_OBJ;
import static ua.com.epam.config.URI.POST_GENRE_SINGLE_OBJ;

public class BaseTest {

    // klov-server fields and methods ------

    private static ExtentHtmlReporter extentHtmlReporter;
    private static ExtentReports extent;
    private static ExtentTest extentTest;

    private void setExtentHtmlReporter() {
        extentHtmlReporter = new ExtentHtmlReporter("Report.html");
        extent = new ExtentReports();
        Calendar calendar = new GregorianCalendar();
        KlovReporter klov = new KlovReporter();
        klov.initMongoDbConnection("ec2-3-15-161-91.us-east-2.compute.amazonaws.com",27017);
        klov.setProjectName("test api books");
        klov.setKlovUrl("http://ec2-3-15-161-91.us-east-2.compute.amazonaws.com/:7777");
        klov.setReportName("SecondTryKlovTest - api books: " + calendar.getTime());
        extent.attachReporter(extentHtmlReporter, klov);
    }

    private void setTestName(String testName) {

        extentTest = extent.createTest(testName);
    }

    private void extentHtmlReporterQuit() {
        if (extentHtmlReporter != null) {
            extentHtmlReporter = null;
        }
    }

    @BeforeSuite
    public void beforeSuite() {

        setExtentHtmlReporter();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        System.out.println(method.getName());
        setTestName(method.getName());
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
            extentTest.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
        } else {
            extentTest.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            extentTest.skip(result.getThrowable());
        }

    }

    @AfterSuite
    public void afterSuiteFlush() {
        extent.flush();
        extentHtmlReporterQuit();
    }

    // ------------------------------------------------------------------------

    private RestClient client = new RestClient();

    // Data source for Authors is changed DataFactory to MySQL DB
    private DataFactory testData = new DataFactory();
    private CleanUpService clean = new CleanUpService(client);

   // protected AuthorService authorService = new AuthorService();
    protected Author randomeAuthor = testData.authors().getRandomOne();
    private List<Author> authorList = testData.authors().getDefaultAuthors();

    protected AuthorService authorService = new AuthorService();
    protected BookService bookService = new BookService();
    protected Book randomBook = testData.books().getRandomBook();
    private List<Book> bookList = testData.books().getDefaultBooks();

    //protected GenreService genreService = new GenreService();
    protected Genre randomGenre = testData.genres().getRandomGenre();
    private List<Genre> genreList = testData.genres().getDefaultGenres();

    protected ValidatorFactory validatorFactory = new ValidatorFactory();

     protected void sendAllAuthors(){
        for(Author a : authorList) {
            client.post(POST_AUTHOR_SINGLE_OBJ, a);
        }
    }

    protected void sendAllGenres(){
        for(Genre genre : genreList) {
            client.post(POST_GENRE_SINGLE_OBJ, genre);
        }
    }

    protected void sendAllBooks(){

        for(Book book : bookList) {
            bookService.postSingleBook(book,
                    authorList.get(new Random().nextInt(10)).getAuthorId(),
                    genreList.get(new Random().nextInt(5)).getGenreId());
        }
    }

    protected void sendPrearrangedBooks(){

        bookService.postSingleBook(bookList.get(0), authorList.get(4).getAuthorId(), genreList.get(1).getGenreId());
        bookService.postSingleBook(bookList.get(1), authorList.get(9).getAuthorId(), genreList.get(3).getGenreId());
        bookService.postSingleBook(bookList.get(2), authorList.get(5).getAuthorId(), genreList.get(0).getGenreId());
        bookService.postSingleBook(bookList.get(3), authorList.get(4).getAuthorId(), genreList.get(3).getGenreId());
        bookService.postSingleBook(bookList.get(4), authorList.get(0).getAuthorId(), genreList.get(4).getGenreId());
        bookService.postSingleBook(bookList.get(5), authorList.get(0).getAuthorId(), genreList.get(3).getGenreId());
        bookService.postSingleBook(bookList.get(6), authorList.get(4).getAuthorId(), genreList.get(0).getGenreId());
        bookService.postSingleBook(bookList.get(7), authorList.get(2).getAuthorId(), genreList.get(4).getGenreId());
        bookService.postSingleBook(bookList.get(8), authorList.get(4).getAuthorId(), genreList.get(1).getGenreId());
        bookService.postSingleBook(bookList.get(9), authorList.get(4).getAuthorId(), genreList.get(1).getGenreId());
        bookService.postSingleBook(bookList.get(10), authorList.get(9).getAuthorId(), genreList.get(1).getGenreId());
        bookService.postSingleBook(bookList.get(11), authorList.get(2).getAuthorId(), genreList.get(3).getGenreId());
        bookService.postSingleBook(bookList.get(12), authorList.get(8).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(13), authorList.get(3).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(14), authorList.get(8).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(15), authorList.get(3).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(16), authorList.get(7).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(17), authorList.get(7).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(18), authorList.get(1).getAuthorId(), genreList.get(2).getGenreId());
        bookService.postSingleBook(bookList.get(19), authorList.get(1).getAuthorId(), genreList.get(2).getGenreId());

    }


    //don't delete this!!!
    @BeforeMethod
    public void reinitialize() {
        client = new RestClient();
        testData = new DataFactory();
        clean = new CleanUpService(client);
    }

    @AfterMethod
    public void cleanUp() {

         clean.authors();
         clean.genres();
         clean.books();
    }
}
