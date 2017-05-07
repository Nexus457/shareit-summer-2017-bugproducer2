package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.restAPI.MediaServiceResult;
import javafx.util.Pair;

import java.util.List;

/**
 * CopyService Class.
 */
public interface CopyService {

    MediaServiceResult addCopy(String user, String code);
    Pair<MediaServiceResult, Copy> getCopy(String identifier, int lfnr);
    List<Copy> getCopies();
    MediaServiceResult updateCopy(String username, String code, int lfnr);


}
