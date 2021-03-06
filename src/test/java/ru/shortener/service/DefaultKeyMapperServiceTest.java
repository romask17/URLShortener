package ru.shortener.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import ru.shortener.model.Link;
import ru.shortener.repository.LinkRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;


public class DefaultKeyMapperServiceTest {

    private static final String KEY = "aabbccdd";
    private static final String LINK_A = "http://example.com";
    private static final String LINK_B = "http://ya.ru";
    private static final String KEY_A = "abc";
    private static final String KEY_B = "cde";
    private static final Long ID_A = 10000000L;
    private static final Long ID_B = 10000001L;

    private KeyMapperService service;

    @Before
    public void init() {
        LinkRepository repository = mock(LinkRepository.class);
        KeyConverterService converterService = mock(DefaultKeyConverterService.class);
        service = new DefaultKeyMapperService(converterService, repository);

        Link LINK_OBJ_A = new Link();
        LINK_OBJ_A.setUrl(LINK_A);
        LINK_OBJ_A.setId(ID_A);

        Link LINK_OBJ_B = new Link();
        LINK_OBJ_B.setUrl(LINK_B);
        LINK_OBJ_B.setId(ID_B);

        Link LINK_SAVE_A = new Link();
        LINK_SAVE_A.setUrl(LINK_A);

        Link LINK_SAVE_B = new Link();
        LINK_SAVE_B.setUrl(LINK_B);

        MockitoAnnotations.initMocks(this);

        when(converterService.keyToId(KEY_A)).thenReturn(ID_A);
        when(converterService.idToKey(ID_A)).thenReturn(KEY_A);
        when(converterService.keyToId(KEY_B)).thenReturn(ID_B);
        when(converterService.idToKey(ID_B)).thenReturn(KEY_B);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.save(LINK_SAVE_A)).thenReturn(LINK_OBJ_A);
        when(repository.save(LINK_SAVE_B)).thenReturn(LINK_OBJ_B);
        when(repository.findById(ID_A)).thenReturn(Optional.of(LINK_OBJ_A));
        when(repository.findById(ID_B)).thenReturn(Optional.of(LINK_OBJ_B));
    }

    @Test
    public void clientCanAddLink() {
        String keyA = service.add(LINK_A);
        assertEquals(LINK_A, service.getLink(keyA).getUrl());
        String keyB = service.add(LINK_B);
        assertEquals(LINK_B, service.getLink(keyB).getUrl());
        assertNotEquals(keyA,keyB);
    }

    @Test(expected = java.lang.RuntimeException.class)
    public void clientCanNotTakeLinkIfKeyIsNotFoundInService() {
        service.getLink(KEY);
    }
}
