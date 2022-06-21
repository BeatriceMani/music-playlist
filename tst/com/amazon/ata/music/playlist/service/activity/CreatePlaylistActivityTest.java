package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import static org.mockito.MockitoAnnotations.initMocks;

public class CreatePlaylistActivityTest {

    private CreatePlaylistActivity createPlaylistActivity;

    @Mock
    private PlaylistDao playlistDao;

   @Mock
    private ModelConverter modelConverter;

    @BeforeEach
    public void setup(){
        initMocks(this);
        createPlaylistActivity = new CreatePlaylistActivity(playlistDao);
    }





    @Test
    void handleRequest_withValidCredentials_createsPlaylistActivity() {
        // GIVEN
        String expectedName = "expectedName";
        String expectedId = "AB123";
        List<String> tags = Lists.newArrayList("tag");
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(expectedId)
                .withName(expectedName)
                .withTags(tags)
                .build();

        when(playlistDao.savePlaylist(any(Playlist.class))).thenAnswer(invocation -> invocation.getArguments()[0]);


        // WHEN
        CreatePlaylistResult result = createPlaylistActivity.handleRequest(request, null);

        // THEN
        assertEquals(request.getName(), result.getPlaylist().getName(), "Expected name to match");


    }

    @Test
    void handleRequest_withInvalidName_throwsException() {
        String invalidName = "invalid'Name";
        String expectedId = "ID123";
        List<String> tags = Lists.newArrayList("tag");
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(expectedId)
                .withName(invalidName)
                .withTags(tags)
                .build();


        assertThrows(InvalidAttributeValueException.class, () -> createPlaylistActivity.handleRequest(request, null));
    }

    @Test
    void handleRequest_withInvalidId_throwsException(){
        String expectedName = "expectedName";
        String invalidId = "AB1'23";
        List<String> tags = Lists.newArrayList("tag");
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .withCustomerId(invalidId)
                .withName(expectedName)
                .withTags(tags)
                .build();
        //WHEN & THEN
        assertThrows(InvalidAttributeValueException.class, () -> createPlaylistActivity.handleRequest(request, null));
    }

}
