package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.jvnet.hk2.internal.Collector;

import java.util.*;
import java.util.stream.Collectors;


import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.discs;

public class CopyServiceImpl implements CopyService {

    public static List<Copy> copies = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    private int booklfnr = 0;
    private int disclfnr = 0;

    public CopyServiceImpl() {

    }

    @Override
    public MediaServiceResult addCopy(String username, String code) {

        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

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
                    disclfnr=copyList.size()+1;

                    if (users.isEmpty()) {
                        User user2 = new User(username);
                        users.add(user2);
                        copies.add(new Copy(d, user2, disclfnr));
                        disclfnr++;
                        mediaServiceResult = MSR_OK;
                    }

                    for (User user1 : users) {
                        if (user1.getUserName().equals(username)) {
                            copies.add(new Copy(d, user1, disclfnr));
                            disclfnr++;
                            mediaServiceResult = MSR_OK;
                            System.out.println("#noduplicate");
                        } else {
                            User user2 = new User(username);
                            users.add(user2);
                            copies.add(new Copy(d, user2, disclfnr));
                            disclfnr++;
                            mediaServiceResult = MSR_OK;
                        }
                    }
                } else {
                    mediaServiceResult = MSR_BAD_REQUEST;
                }
            }
        } else if (Isbn.isValid(code)) {
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
                    booklfnr=copyList.size()+1;

                    if (users.isEmpty()) {
                        User user2 = new User(username);
                        users.add(user2);
                        copies.add(new Copy(b, user2, booklfnr));
                        booklfnr++;
                        mediaServiceResult = MSR_OK;
                    } else {
                        for (User user1 : users) {
                            if (user1.getUserName().equals(username)) {
                                copies.add(new Copy(b, user1, booklfnr));
                                booklfnr++;
                                mediaServiceResult = MSR_OK;
                            } else {
                                User user2 = new User(username);
                                users.add(user2);
                                copies.add(new Copy(b, user2, booklfnr));
                                booklfnr++;
                                mediaServiceResult = MSR_OK;
                            }
                        }
                    }
                } else {
                    mediaServiceResult = MSR_BAD_REQUEST;
                }
            }
        } else {
            mediaServiceResult = MSR_NO_CONTENT;
        }

        return mediaServiceResult;
    }


    @Override
    public Pair<MediaServiceResult, Copy> getCopy(String identifier) {
        return null;
    }

    @Override
    public List<Copy> getCopies() {
        return copies;
    }

    @Override
    public MediaServiceResult updateCopy(String user, String code) {
        return null;
    }
}
