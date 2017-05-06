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

public class CopyServiceImpl implements CopyService {

    public static List<Copy> copies = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    private int booklfnr = 0;
    private int disclfnr = 0;

    public CopyServiceImpl() {

    }

    @Override
    public MediaServiceResult addCopy(String username, String code) {
        boolean noUserFound = false;

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
                    return MSR_BAD_REQUEST;
                }
            }
        } else if (Isbn.isValid(code)) {
            MediaServiceResult x = checkBook(username, code, noUserFound);
            if (x != null) return x;
        } else {
            return MSR_NO_CONTENT;
        }

        return MSR_INTERNAL_SERVER_ERROR;
    }

    private MediaServiceResult addDiscCopy(Disc d, User user1) {
        copies.add(new Copy(d, user1, disclfnr));
        disclfnr++;
        return MSR_OK;
    }

    private MediaServiceResult addUserAndAddDiscCopy(String username, Disc d) {
        User user2 = new User(username);
        users.add(user2);
        return addDiscCopy(d, user2);
    }

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
                if (counter == books.size()) return MSR_BAD_REQUEST;
            }
            counter++;
        }
        return null;
    }

    private MediaServiceResult addCopy(Book b, User user1) {
        copies.add(new Copy(b, user1, booklfnr));
        booklfnr++;
        return MSR_OK;
    }

    private MediaServiceResult createUserAndAddBookCopy(String username, Book b) {
        User user2 = new User(username);
        users.add(user2);
        return addCopy(b, user2);
    }


    @Override
    public Pair<MediaServiceResult, Copy> getCopy(String identifier, int lfnr) {
        if (EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(identifier)) {
            List<Copy> resultList = copies
                    .stream()
                    .map(copy -> {
                        if (copy.getMedium() instanceof Disc) {
                            Disc disc = (Disc) copy.getMedium();
                            if (disc.getBarcode().equals(identifier) && copy.getLfnr() == lfnr) {
                                return copy;
                            }

                        }
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());

            if (resultList.size() != 1) {
                return new Pair<>(MSR_BAD_REQUEST, null);
            } else {
                return new Pair<>(MSR_OK, resultList.get(0));
            }

        } else if (Isbn.isValid(identifier)) {
            List<Copy> resultList = copies
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
    public MediaServiceResult updateCopy(String user, String code, int lfnr) {
        return null;
    }


}
