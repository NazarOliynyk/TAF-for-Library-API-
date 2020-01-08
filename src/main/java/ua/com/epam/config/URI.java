package ua.com.epam.config;

public interface URI {
    DataProp dp = new DataProp();
    String BASE_URI = dp.apiProtocol() + "://" + dp.apiHost() + ":" + dp.apiPort();
    String AUTHENTICATION_URI = "/api/library/login";
    //Author
    String GET_AUTHOR_SINGLE_OBJ = "/api/library/author/%s";
    String GET_AUTHOR_OF_BOOK_OBJ = "/api/library/book/%s/author";
    String GET_ALL_AUTHORS_ARR = "/api/library/authors";
    String SEARCH_FOR_EXISTED_AUTHORS_ARR = "/api/library/authors/search";
    String GET_ALL_AUTHORS_IN_GENRE_ARR = "/api/library/genre/%s/authors";
    String POST_AUTHOR_SINGLE_OBJ = "/api/library/author";
    String PUT_AUTHOR_SINGLE_OBJ = "/api/library/author/%s";
    String DELETE_AUTHOR_SINGLE_OBJ = "/api/library/author/%s";

    String POST_BOOK_SINGLE_OBJ = "/api/library/book/%s/%s";
    String GET_BOOK_SINGLE_OBJ = "/api/library/book/%s";
    String GET_ALL_BOOKS_ARR = "/api/library/books";
    String DELETE_BOOK_SINGLE_OBJ = "/api/library/book/%s";
    String GET_ALL_BOOKS_BY_AUTHOR_ARR = "/api/library/author/%s/books";

//    /library/author/3232/books?orderType=asc&sortBy=bookId

    String GET_GENRE_SINGLE_OBJ = "/api/library/genre/%s";
    String POST_GENRE_SINGLE_OBJ = "/api/library/genre";
    String GET_ALL_GENRES_ARR = "/api/library/genres";
    String DELETE_GENRE_SINGLE_OBJ = "/api/library/genre/%s";
    String GET_GENRE_BY_BOOK = "/api/library/book/%s/genre";
    String PUT_GENRE_SINGLE_OBJ = "/api/library/genre/%s";
    String SEARCH_FOR_EXISTING_GENRE_ARR = "/api/library/genres/search";
}
