package org.eldron.shorty.service;

import org.eldron.shorty.exception.UrlNotFoundException;
import org.eldron.shorty.hash.UrlHash;
import org.eldron.shorty.repository.UrlRepository;
import org.eldron.shorty.vo.Url;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    public void whenUrlExists_thenGetOriginalUrl() throws UrlNotFoundException {
        final var urlId = "urlid";
        final var originalUrl = "www.google.com";
        when(urlRepository.findById(urlId)).thenReturn(Optional.of(UrlHash.builder()
                .id(urlId)
                .originalUrl(originalUrl)
                .build()));

        final Url url = urlService.getOriginalUrl(urlId);

        assertThat(url).isNotNull();
        assertThat(url.getOriginalUrl()).isNotNull();
        assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test(expected = UrlNotFoundException.class)
    public void whenUrlDoesntExist_thenThrowException() throws UrlNotFoundException {
        final var urlId = "urlid";
        when(urlRepository.findById(urlId)).thenReturn(Optional.empty());

        urlService.getOriginalUrl(urlId);
    }

    @Test
    public void shortenUrl_thenSaveUrl() {
        final var url = "www.google.com";
        when(urlRepository.save(any(UrlHash.class))).thenReturn(any(UrlHash.class));

        final Url shortenedUrl = urlService.shortenUrl(url);

        assertThat(shortenedUrl).isNotNull();
        assertThat(shortenedUrl.getOriginalUrl()).isEqualTo(url);
        assertThat(shortenedUrl.getId().length()).isEqualTo(10);
    }

}