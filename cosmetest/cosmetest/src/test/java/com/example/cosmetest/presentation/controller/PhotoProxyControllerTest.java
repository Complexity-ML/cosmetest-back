package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.PhotoProxyService;
import com.example.cosmetest.business.service.VolontaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.net.SocketTimeoutException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoProxyControllerTest {

    @Mock VolontaireService volontaireService;
    @Mock AuditLogService auditLogService;
    @Mock PhotoProxyService photoProxyService;

    private VolontaireController controller;

    @BeforeEach
    void setUp() {
        controller = new VolontaireController(volontaireService, auditLogService, photoProxyService);
    }

    @Test
    void imageEndpointDelegatesDownloadToHardenedProxy() {
        when(volontaireService.getVolontairePhoto(7, "face")).thenReturn(photoInfo("face.JPG"));
        when(photoProxyService.fetch("face.JPG"))
                .thenReturn(new PhotoProxyService.PhotoPayload(new byte[]{1, 2}, MediaType.IMAGE_JPEG));

        var response = controller.getVolontairePhotoImage(7, "face");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
        assertThat(response.getBody()).containsExactly(1, 2);
    }

    @Test
    void thumbnailEndpointReturnsGatewayTimeoutForUpstreamTimeout() {
        when(volontaireService.getVolontairePhoto(7, "face")).thenReturn(photoInfo("face.JPG"));
        when(photoProxyService.fetch("face.JPG")).thenThrow(new PhotoProxyService.PhotoTimeoutException(
                "timeout", new SocketTimeoutException()));

        var response = controller.getVolontairePhotoThumbnail(7, "face");

        assertThat(response.getStatusCode().value()).isEqualTo(504);
    }

    @Test
    void imageEndpointReturnsBadGatewayForUpstreamFailureOrOversizePayload() {
        when(volontaireService.getVolontairePhoto(7, "face")).thenReturn(photoInfo("face.JPG"));
        when(photoProxyService.fetch("face.JPG"))
                .thenThrow(new PhotoProxyService.PhotoUpstreamException("upstream"))
                .thenThrow(new PhotoProxyService.PhotoTooLargeException("oversize"));

        assertThat(controller.getVolontairePhotoImage(7, "face").getStatusCode().value()).isEqualTo(502);
        assertThat(controller.getVolontairePhotoImage(7, "face").getStatusCode().value()).isEqualTo(502);
    }

    @Test
    void imageEndpointReturnsBadRequestForRejectedPath() {
        when(volontaireService.getVolontairePhoto(7, "face")).thenReturn(photoInfo("../secret.JPG"));
        when(photoProxyService.fetch("../secret.JPG"))
                .thenThrow(new PhotoProxyService.InvalidPhotoPathException("invalid"));

        assertThat(controller.getVolontairePhotoImage(7, "face").getStatusCode().value()).isEqualTo(400);
    }

    private Map<String, Object> photoInfo(String fileName) {
        return Map.of("exists", true, "fileName", fileName);
    }
}
