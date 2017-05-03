package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Johannes Arzt on 25.04.17.
 */

public interface CopyService {

    MediaServiceResult addCopy(Book book, String user);
  //  MediaServiceResult addCopy(Disc disc, String user);

    Pair<MediaServiceResult, Copy> getCopy(String identifier);
    List<Copy> getCopies();
}
