package edu.hm.bugproducer;


import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Book;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.media.MediaService;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static edu.hm.bugproducer.Status.MediaServiceResult.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class MockTestDisc {
    private static final String USER_NAME = "Joh";
    private static final String NAME = "TestName1";
    private static final String NAME_ALT = "TestName2";
    private static final String TITLE = "TestTitle1";
    private static final String TITLE_ALT = "TestTitle2";
    private static final String ISBN = "3-446-193138";
    private static final String ISBN_ALT = "0-7475-51006";
    private static final String EAN = "9783815820865";
    private static final String EAN_ALT = "9783827317100";
    private static final int FSK = 12;
    private static final int FSK_ALT = 18;

    @After
    public void clearList(){
        MediaService mediaService = new MediaServiceImpl();
        mediaService.deleteAll();
    }

    @Test
    public void testAddDisc() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_OK, "ok");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscWrongTitle() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn("");
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscWrongDirector() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn("");
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscInvalidEAN() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn("111111111");
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode was not valid");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscWrongEAN() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn("");
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscWrongFSK() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(-1);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted, status);
    }

    @Test
    public void testAddDiscDuplicate() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        mediaService.addDisc(disc);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "The disc is already in the system. No duplicate allowed");
        assertEquals(wanted, status);
    }

    @Test
    public void testGetDisc() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        mediaService.addDisc(disc);
        Pair<StatusMgnt, Disc> actual = mediaService.getDisc(disc.getBarcode());
        Pair<StatusMgnt, Disc> wanted = new Pair<>(new StatusMgnt(MSR_OK, "ok"), disc);
        assertEquals(wanted.getKey(), actual.getKey());
        assertEquals(wanted.getValue(), actual.getValue());
    }

    @Test
    public void testGetDiscNotFound() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        Pair<StatusMgnt, Disc> actual = mediaService.getDisc("123123");
        Pair<StatusMgnt, Disc> wanted = new Pair<>(new StatusMgnt(MSR_NOT_FOUND, "The disc you have searched for is not in the system!"), null);
        assertEquals(wanted, actual);
    }

    @Test
    public void testUpdateDisc() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);

        mediaService.addDisc(disc);

        Disc updateDisc = Mockito.mock(Disc.class);
        Mockito.when(updateDisc.getTitle()).thenReturn(TITLE_ALT);
        Mockito.when(updateDisc.getDirector()).thenReturn(NAME_ALT);
        Mockito.when(updateDisc.getFsk()).thenReturn(FSK_ALT);

        StatusMgnt actual = mediaService.updateDisc(EAN,updateDisc);
        StatusMgnt wanted = new StatusMgnt(MSR_OK, "ok");
        assertEquals(wanted,actual);
    }

    @Test
    public void testUpdateDiscEmpty() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);

        mediaService.addDisc(disc);

        Disc updateDisc = Mockito.mock(Disc.class);
        Mockito.when(updateDisc.getTitle()).thenReturn("");
        Mockito.when(updateDisc.getDirector()).thenReturn("");
        Mockito.when(updateDisc.getFsk()).thenReturn(-1);

        StatusMgnt actual = mediaService.updateDisc(EAN,updateDisc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Director, Title and FSK are empty!");;
        assertEquals(wanted,actual);
    }

    @Test
    public void testUpdateNonExistingDisc() {
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);

        mediaService.addDisc(disc);

        Disc updateDisc = Mockito.mock(Disc.class);
        Mockito.when(updateDisc.getTitle()).thenReturn(TITLE_ALT);
        Mockito.when(updateDisc.getDirector()).thenReturn(NAME_ALT);
        Mockito.when(updateDisc.getFsk()).thenReturn(FSK_ALT);

        StatusMgnt actual = mediaService.updateDisc(EAN_ALT,updateDisc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "The disc you want to update is not in the system!");
        assertEquals(wanted,actual);
    }




}
