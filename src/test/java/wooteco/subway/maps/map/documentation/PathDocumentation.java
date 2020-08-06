package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.dto.StationResponse;

@WebMvcTest(controllers = MapController.class)
public class PathDocumentation extends Documentation {

    protected TokenResponse tokenResponse;
    @MockBean
    private MapService mapService;

    @BeforeEach
    public void setUp(
        WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        StationResponse gyodae = new StationResponse(1L, "교대역", now(), now());
        StationResponse gangnam = new StationResponse(2L, "강남역", now(), now());
        StationResponse yangjae = new StationResponse(3L, "양재역", now(), now());
        List<StationResponse> stations = Arrays.asList(gyodae, gangnam, yangjae);
        int duration = 3;
        int distance = 4;
        int fare = 1250;

        PathResponse pathResponse = new PathResponse(stations, duration, distance);
        when(mapService.findPath(any(), any(), any())).thenReturn(pathResponse);

        given().log().all().
            header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", 1, 2, "DISTANCE").
            then().
            log().all().
            apply(document("paths/distance",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer auth credentials")),
                responseFields(
                    fieldWithPath("stations[]").type(JsonFieldType.ARRAY)
                        .description("최단 거리 - 경로(역)"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NUMBER)
                        .description("최단 거리 - 경로(역) ID"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING)
                        .description("최단 거리 - 경로(역) 이름"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER)
                        .description("최단 거리 - 소요 거리"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER)
                        .description("최단 거리 - 소요 시간"),
                    fieldWithPath("fare").type(JsonFieldType.NUMBER).description("최단 거리 - 금액")))).
            extract();
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        StationResponse gyodae = new StationResponse(1L, "교대역", now(), now());
        StationResponse gangnam = new StationResponse(2L, "강남역", now(), now());
        StationResponse yangjae = new StationResponse(3L, "양재역", now(), now());
        List<StationResponse> stations = Arrays.asList(gyodae, gangnam, yangjae);
        int duration = 3;
        int distance = 4;
        int fare = 1250;

        PathResponse pathResponse = new PathResponse(stations, duration, distance);
        when(mapService.findPath(any(), any(), any())).thenReturn(pathResponse);

        given().log().all().
            header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", 1, 2, "DURATION").
            then().
            log().all().
            apply(document("paths/duration",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer auth credentials")),
                responseFields(
                    fieldWithPath("stations[]").type(JsonFieldType.ARRAY)
                        .description("최소 시간 - 경로(역)"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NUMBER)
                        .description("최소 시간 - 경로(역) ID"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING)
                        .description("최소 시간 - 경로(역) 이름"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER)
                        .description("최소 시간 - 소요 거리"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER)
                        .description("최소 시간 - 소요 시간"),
                    fieldWithPath("fare").type(JsonFieldType.NUMBER).description("최단 거리 - 금액")))).
            extract();
    }
}
