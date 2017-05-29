package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import java.util.*;
import java.util.stream.Collectors;


import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.books;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.discs;

/**
 * CopyServiceImpl Class.
 *
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
public class CopyServiceImpl implements CopyService {
    /**
     * ArrayList that contains the copies.
     */
    private static List<Copy> copies = new ArrayList<>();
    /**
     * ArrayList that contains the users.
     */
    private static List<User> users = new ArrayList<>();
    /**
     * running number of copies of books with value 0.
     */
    private int booklfnr = 0;
    /**
     * running number of copies of discs with value 0.
     */
    private int disclfnr = 0;

    /**
     * CopyServiceImpl Constructor.
     */
    public CopyServiceImpl() {

    }


    @Override
    public MediaServiceResult addCopy(String username, String code) {
        boolean noUserFound = false;
        int discCounter = 0;
// PLEASE DELETE ME AS WELL
        // MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(code)) {

            for (Disc d : MediaServiceImpl.discs) {
                if (d.getBarcode().equals(code)) {

                    List<Disc> copyList = copies
                            .stream()
                            .map(disc -> {
                                if (disc.getMedium() instanceof Disc) {
                                    return (Disc) disc.getMedium();
                                } else {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .filter(disc -> disc.getBarcode().equals(code))
                            .collect(Collectors.toList());
                    disclfnr = copyList.size() + 1;

                    if (users.isEmpty()) {
                        return addUserAndAddDiscCopy(username, d);
                    }

                    for (User user1 : users) {
                        if (user1.getUserName().equals(username)) {
                            return addDiscCopy(d, user1);
                        } else {
                            noUserFound = true;
                        }
                    }
                    if (noUserFound) {
                        return addUserAndAddDiscCopy(username, d);
                    }
                } else {
                    if (discCounter == discs.size()) {
                        return MSR_BAD_REQUEST;
                    }
                }
                discCounter++;
            }
        } else if (Isbn.isValid(code)) {
            MediaServiceResult x = checkBook(username, code, noUserFound);
            if (x != null) {
                return x;
            }
        } else {
            return MSR_NO_CONTENT;
        }

        return MSR_INTERNAL_SERVER_ERROR;
    }

    /**
     * private addDiscCopy method.
     * adds a new copy of a disc with it´s user
     *
     * @param d     a disc Object
     * @param user1 user who borrows it
     * @return statusCode OK
     */
    private MediaServiceResult addDiscCopy(Disc d, User user1) {
        copies.add(new Copy(d, user1, disclfnr));
        disclfnr++;
        return MSR_OK;
    }

    /**
     * private addUserAndAddDiscCopy method.
     * adds a new user to the list and creates a new copy of the disc
     *
     * @param username user who wants to borrow a disc but was not in the list
     * @param d        disc object
     * @return the executing of addDiscCopy method
     */
    private MediaServiceResult addUserAndAddDiscCopy(String username, Disc d) {
        User user2 = new User(username);
        users.add(user2);
        return addDiscCopy(d, user2);
    }

    /**
     * private checkBook method.
     * after checking if the isbn is valid, it´s analyzing if the user is already in the list
     *
     * @param username    name of person
     * @param code        isbn of book
     * @param noUserFound check variable
     * @return it either returns that a book was borrowed by an already exist user, or from a new user or it returns
     * a status code of what happened or null
     */
    private MediaServiceResult checkBook(String username, String code, boolean noUserFound) {
        int counter = 0;
        for (Book b : MediaServiceImpl.books) {
            if (b.getIsbn().equals(code)) {
                List<Book> copyList = copies
                        .stream()
                        .map(book -> {
                            if (book.getMedium() instanceof Book) {
                                return (Book) book.getMedium();
                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .filter(book -> book.getIsbn().equals(code))
                        .collect(Collectors.toList());
                booklfnr = copyList.size() + 1;

                if (users.isEmpty()) {
                    return createUserAndAddBookCopy(username, b);
                } else {
                    for (User user1 : users) {
                        if (user1.getUserName().equals(username)) {
                            return addCopy(b, user1);
                        } else {
                            noUserFound = true;
                        }
                    }
                    if (noUserFound) {
                        return createUserAndAddBookCopy(username, b);
                    }
                }
            } else {
                if (counter == books.size()) {
                    return MSR_BAD_REQUEST;
                }
            }
            counter++;
        }
        return null;
    }

    /**
     * private addCopy method.
     * adds a new copy of book and it´s user
     *
     * @param b     book object
     * @param user1 name of person
     * @return statusCode OK
     */
    private MediaServiceResult addCopy(Book b, User user1) {
        copies.add(new Copy(b, user1, booklfnr));
        booklfnr++;
        return MSR_OK;
    }

    /**
     * private addUserAndAddBookCopy method.
     * adds a new user to the list and creates a new copy of the book
     *
     * @param username user who wants to borrow a book but was not in the list
     * @param b        book object
     * @return the executing of addCopy method
     */
    private MediaServiceResult createUserAndAddBookCopy(String username, Book b) {
        User user2 = new User(username);
        users.add(user2);
        return addCopy(b, user2);
    }


    @Override
    public Pair<MediaServiceResult, Copy> getCopy(String identifier, int lfnr) {
        if (EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(identifier)) {
            List<Copy> resultList = getSpecialDiscCopies(identifier, lfnr);
            if (resultList.size() != 1) {
                return new Pair<>(MSR_BAD_REQUEST, null);
            } else {
                return new Pair<>(MSR_OK, resultList.get(0));
            }

        } else if (Isbn.isValid(identifier)) {
            List<Copy> resultList = getSpecialBookCopy(identifier, lfnr);
            if (resultList.size() != 1) {
                return new Pair<>(MSR_BAD_REQUEST, null);
            } else {
                return new Pair<>(MSR_OK, resultList.get(0));
            }
        }
        return new Pair<>(MSR_INTERNAL_SERVER_ERROR, null);


    }


    @Override
    public List<Copy> getCopies() {
        return copies;
    }


    @Override
    public MediaServiceResult updateCopy(String username, String code, int lfnr) {
        List<User> tmpUserList;
        if (EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(code)) {
            List<Copy> resultList = getSpecialDiscCopies(code, lfnr);
            if (resultList.size() != 1) {
                return MSR_BAD_REQUEST;
            } else {
                checkUser(username, resultList);
                return MSR_OK;
            }

        } else if (Isbn.isValid(code)) {
            List<Copy> resultList = getSpecialBookCopy(code, lfnr);
            if (resultList.size() != 1) {
                return MSR_BAD_REQUEST;
            } else {
                checkUser(username, resultList);
                return MSR_OK;
            }
        }
        return MSR_INTERNAL_SERVER_ERROR;

    }

    /**
     * checkUser method.
     * if user list is empty, it creates a new user, else it sets the user into the user list
     *
     * @param username   name of the person
     * @param resultList the user list
     */
    private void checkUser(String username, List<Copy> resultList) {
        List<User> tmpUserList;
        tmpUserList = users.stream().filter(user -> user.getUserName().equals(username)).collect(Collectors.toList());

        if (tmpUserList.isEmpty()) {
            User newUser = new User(username);
            users.add(newUser);
            resultList.get(0).setUser(newUser);
        } else {
            User user = tmpUserList.get(0);
            resultList.get(0).setUser(user);
        }
    }

    /**
     * getSpecialDiscCopies method.
     * used the get a specific copy of disc if it exist
     *
     * @param code barcode of disc
     * @param lfnr running number of copy of disc
     * @return copy if it exist, else null
     */
    private List<Copy> getSpecialDiscCopies(String code, int lfnr) {
        return copies
                .stream()
                .map(copy -> {
                    if (copy.getMedium() instanceof Disc) {
                        Disc disc = (Disc) copy.getMedium();
                        if (disc.getBarcode().equals(code) && copy.getLfnr() == lfnr) {
                            return copy;
                        }

                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * getSpecialBookCopies method.
     * used the get a specific copy of book if it exist
     *
     * @param identifier isbn of book
     * @param lfnr running number of copy of book
     * @return copy if it exist, else null
     */
    private List<Copy> getSpecialBookCopy(String identifier, int lfnr) {
        return copies
                .stream()
                .map(copy -> {
                    if (copy.getMedium() instanceof Book) {
                        Book book = (Book) copy.getMedium();
                        if (book.getIsbn().equals(identifier) && copy.getLfnr() == lfnr) {
                            return copy;
                        }
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }


}
