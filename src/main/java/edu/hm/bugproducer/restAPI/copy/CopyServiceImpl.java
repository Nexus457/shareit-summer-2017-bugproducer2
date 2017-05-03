package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


import static edu.hm.bugproducer.restAPI.MediaServiceResult.*;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.books;
import static edu.hm.bugproducer.restAPI.media.MediaServiceImpl.discs;

public class CopyServiceImpl implements CopyService {

    public static List<Copy> copies = new ArrayList<>();

    @Override
    public MediaServiceResult addCopy(Book book, String user) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;


        ListIterator<Book> lir = books.listIterator();

        while (lir.hasNext()) {
            if (lir.next().getIsbn().equals(book.getIsbn())) {
                mediaServiceResult = MSR_OK;
                //todo nur zu testzwecken
                copies.add(new Copy(book, new User(user))); //nicht sauber!!
            } else {
                mediaServiceResult = MSR_BAD_REQUEST;
            }
        }
        return mediaServiceResult;
    }

    @Override
    public MediaServiceResult addCopy(Disc disc, String user) {
        MediaServiceResult mediaServiceResult = MSR_INTERNAL_SERVER_ERROR;


            ListIterator<Disc> lir = discs.listIterator();

            while (lir.hasNext()) {
                if (lir.next().getBarcode().equals(disc.getBarcode())) {
                    mediaServiceResult = MSR_OK;
                    //todo nur zu testzwecken
                    copies.add(new Copy(disc, new User(user))); //nicht sauber
                } else {
                    mediaServiceResult = MSR_BAD_REQUEST;
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
