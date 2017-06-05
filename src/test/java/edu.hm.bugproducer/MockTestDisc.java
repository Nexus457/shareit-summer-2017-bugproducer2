package edu.hm.bugproducer;


import edu.hm.bugproducer.Status.StatusMgnt;
import edu.hm.bugproducer.models.Disc;
import edu.hm.bugproducer.restAPI.media.MediaService;
import edu.hm.bugproducer.restAPI.media.MediaServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;
import static edu.hm.bugproducer.Status.MediaServiceResult.*;
import static org.junit.Assert.assertEquals;


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

    @Test
    public void testAddDisc(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_OK, "ok");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDiscWrongTitle(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn("");
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDiscWrongDirector(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn("");
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDiscInvalidEAN(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn("111111111");
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode was not valid");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDiscWrongFSK(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(-1);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "Barcode or director or title was empty or FSK was less than 0 ");
        assertEquals(wanted,status);
    }

    @Test
    public void testAddDiscDuplicate(){
        MediaService mediaService = new MediaServiceImpl();
        Disc disc = Mockito.mock(Disc.class);
        Mockito.when(disc.getTitle()).thenReturn(TITLE);
        Mockito.when(disc.getDirector()).thenReturn(NAME);
        Mockito.when(disc.getBarcode()).thenReturn(EAN);
        Mockito.when(disc.getFsk()).thenReturn(FSK);
        mediaService.addDisc(disc);
        StatusMgnt status = mediaService.addDisc(disc);
        StatusMgnt wanted = new StatusMgnt(MSR_BAD_REQUEST, "The disc is already in the system. No duplicate allowed");
        assertEquals(wanted,status);
    }

}
