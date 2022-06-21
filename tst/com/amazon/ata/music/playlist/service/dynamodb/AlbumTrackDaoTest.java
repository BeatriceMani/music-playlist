package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.exceptions.AlbumTrackNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AlbumTrackDaoTest {
    private AlbumTrackDao albumTrackDao;

    @Mock
    DynamoDBMapper mapper;

    @BeforeEach
    private void setUp(){
        MockitoAnnotations.initMocks(this);
        albumTrackDao = new AlbumTrackDao(mapper);
    }

    @Test
    void getAlbumTrack_withNonExistentCredentials_throwsAlbumTrackNotFoundException() {
        // GIVEN
        String nonExsistentAsin = "invalid asin";
        int trackNumber = 0;
        when(mapper.load(AlbumTrack.class, nonExsistentAsin, trackNumber)).thenReturn(null);

        // WHEN + THEN

        assertThrows(AlbumTrackNotFoundException.class, () -> albumTrackDao.getAlbumTrack(nonExsistentAsin, trackNumber));
    }

}
