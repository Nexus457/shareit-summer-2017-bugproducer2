package edu.hm.bugproducer.restAPI.copy;

import edu.hm.bugproducer.models.*;
import edu.hm.bugproducer.Status.MediaServiceResult;
import javafx.util.Pair;

import java.util.List;

/**
 * CopyService Class.
 */
public interface CopyService {

    /**
     * addCopy method of type MediaServiceResult.
     * after checking if the barcode is valid, it´s analyzing if the user is already in the list
     *
     * @param user name of person who wants to borrow a disc
     * @param code     the unique number of a disc
     * @return it either returns that a disc was borrowed by an already exist user, or from a new user or it returns
     * a status code of what happened
     */
    MediaServiceResult addCopy(String user, String code);
    /**
     * getCopy method.
     * checking if barcode is valid and returns the copy if it exist, the same with isbn
     *
     * @param identifier code that identifies a book or a disc
     * @param lfnr       running number for a copy
     * @return statusCode OK and the copy or BAD_REQUEST and null
     */
    Pair<MediaServiceResult, Copy> getCopy(String identifier, int lfnr);
    /**
     * getCopies method.
     * getter for copies list
     *
     * @return list of copies
     */
    List<Copy> getCopies();
    /**
     * updateCopy method.
     * checking if barcode is valid and updates the copy if it exist,  if barcode is invalid it checks if isbn is valid
     * and update that book if it exists
     *
     * @param username name of person
     * @param code     identifier number of book or disc
     * @param lfnr     running number of a copy
     * @return statusCode OK, BAD_REQUEST or INTERNAL_SERVER_ERROR
     */
    MediaServiceResult updateCopy(String username, String code, int lfnr);


}
