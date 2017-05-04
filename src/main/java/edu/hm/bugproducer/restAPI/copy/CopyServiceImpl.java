package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.Utils.Isbn;
import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import javafx.util.Pair;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.discs;

public class CopyServiceImpl implements CopyService {

    public static List<Copy> copies = new ArrayList<>();
    public static List<User> users = new ArrayList<>();
    private int lfnr=0;

    public CopyServiceImpl() {

    }

    @Override
    public MediaServiceResult addCopy(String username, String code) {

        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (EAN13CheckDigit.EAN13_CHECK_DIGIT.isValid(code)) {

            for (Disc d : MediaServiceImpl.discs) {
                if (d.getBarcode().equals(code)) {

                    for (User user1 : users) {
                        if (user1.getUserName().equals(username)) {
                            copies.add(new Copy(d, user1,lfnr));
                            lfnr++;
                            mediaServiceResult = MSR_OK;
                            System.out.println("#noduplicate");
                        } else {
                            User user2 = new User(username);
                            users.add(user2);
                            copies.add(new Copy(d, user2,lfnr));
                            lfnr++;
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

                    if (users.isEmpty()) {
                        User user2 = new User(username);
                        users.add(user2);
                        copies.add(new Copy(b, user2,lfnr));
                        lfnr++;
                        mediaServiceResult = MSR_OK;
                    } else {
                        for (User user1 : users) {
                            if (user1.getUserName().equals(username)) {
                                copies.add(new Copy(b, user1,lfnr));
                                lfnr++;
                                mediaServiceResult = MSR_OK;
                            } else {
                                User user2 = new User(username);
                                users.add(user2);
                                copies.add(new Copy(b, user2,lfnr));
                                lfnr++;
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
        return null;
    }

    @Override
    public MediaServiceResult updateCopy(String user, String code) {
        return null;
    }
}
