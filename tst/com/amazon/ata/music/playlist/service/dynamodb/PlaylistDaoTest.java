package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PlaylistDaoTest {
    private PlaylistDao playlistDao;

    @Mock
    private DynamoDBMapper mapper;

    @BeforeEach
    private void setup(){
        initMocks(this);
        playlistDao = new PlaylistDao(mapper);
    }

    @Test
    void savePlaylist_withValidCredentials_returnsPlaylist(){
        // GIVEN
        String id = "123";
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setTags(new HashSet<>());
        playlist.setName("abc");
        playlist.setSongCount(0);
        doNothing().when(mapper).save(playlist);

        // WHEN
        Playlist result = playlistDao.savePlaylist(playlist);

        // THEN
        assertEquals(playlist.getCustomerId(), result.getCustomerId(), "Expected playlist attributes to match");
        assertEquals(playlist.getName(), result.getName(), "Expected playlist attribute Name to match");
        assertEquals(playlist.getId(), result.getId(), "Expected playlist attribute ID to match");

    }

    @Test
    void getPlaylist_withValidID_returnsPlaylist(){
        // GIVEN
        String id = "123";
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setTags(new HashSet<>());
        playlist.setName("abc");
        playlist.setSongCount(0);
        when(mapper.load(Playlist.class, id)).thenReturn(playlist);

        // WHEN
        Playlist result = playlistDao.getPlaylist(id);

        // THEN

        assertEquals(id, result.getId(), "Expected playlist to be returned");
        assertEquals("abc", result.getName(), "Expected playlist Name to match");
    }

    @Test
    void getPlaylist_withInvalidlID_throwsException(){
        // GIVEN
        String nonexistantPlaylistID = "no such name";
        // WHEN
        when(mapper.load(Playlist.class, nonexistantPlaylistID)).thenReturn(null);

        // assertThrows(NullPointerException.class, () -> memberDao.getMember(null))
        assertThrows(PlaylistNotFoundException.class, () -> playlistDao.getPlaylist(nonexistantPlaylistID));
    }



}
