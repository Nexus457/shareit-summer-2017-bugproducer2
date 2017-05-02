package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.books;

public class CopyServiceImpl implements CopyService {

    public static List<Copy> copies = new ArrayList<>();

    @Override
    public MediaServiceResult addCopy(Medium medium, User user) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;

        if (medium instanceof Book) {
            Book book = (Book) medium;
            ListIterator<Book> lir = books.listIterator();

            while (lir.hasNext()) {
                if (lir.next().getIsbn().equals(book.getIsbn())) {
                    mediaServiceResult = MSR_OK;
                    copies.add(new Copy(medium, user));
                } else {
                    mediaServiceResult = MSR_BAD_REQUEST;
                }
            }
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
}
