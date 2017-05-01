package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CopyServiceImpl implements CopyService {


    public static List<Book> books = new ArrayList<>();
    public static List<Disc> discs = new ArrayList<>();


    @Override
    public MediaServiceResult addCopy(Medium medium, User user) {
        return null;
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
