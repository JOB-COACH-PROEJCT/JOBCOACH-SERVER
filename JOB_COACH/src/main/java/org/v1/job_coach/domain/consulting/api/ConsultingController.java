package org.v1.job_coach.domain.consulting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.chatroom.application.ChatRoomService;
import org.v1.job_coach.domain.chatroom.dto.response.ChatRoomResponseDto;
import org.v1.job_coach.domain.consulting.application.ConsultingService;
import org.v1.job_coach.domain.consulting.dto.response.ConsultingResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;
import org.v1.job_coach.user.domain.User;

@Slf4j
@Tag(name = "Consulting", description = "컨설팅 API")
@RestController
@RequestMapping("/api/v1/consulting")
public class ConsultingController {

    private final ConsultingService consultingService;
    private final ChatRoomService chatRoomService;;

    @Autowired
    public ConsultingController(ConsultingService consultingService, ChatRoomService chatRoomService) {
        this.consultingService = consultingService;
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/room")
    @Operation(summary = "사용자 면접 채팅방 반환 API", description = "모의면접 채팅방 리스트를 반환하는 API입니다. 페이징을 포함합니다."
            + "query string으로 page 번호를 주세요")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true),
            @Parameter(name = "page", description = "페이지 번호, 0이상이어야 함, query string")})
    public ResponseEntity<?> getChatroom(@AuthenticationPrincipal User user,
                                         @RequestParam(value = "page", defaultValue = "0") int page) {
        ResultResponseDto<Page<?>> chatRooms = chatRoomService.getChatRooms(user, page);
        return ResponseEntity.status(HttpStatus.OK).body(chatRooms);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "해당 채팅방에서 특정 페이지의 질문, 답변, 컨설팅 가져오는 API", description = "해당 채팅방의 컨설팅 리스트를 반환하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> getConsultingByPage(@PathVariable Long roomId,
                                                 @AuthenticationPrincipal User user,
                                                 @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<ConsultingResponseDto> consulting = consultingService.getConsultingByChatRoom(roomId, page, user);

        return ResponseEntity.status(HttpStatus.OK).body(consulting);
    }

    @DeleteMapping("/room/{roomId}")
    @Operation(summary = "면접 채팅방 삭제 API", description = "면접채팅방을 삭제하는 API입니다.")
    @Parameters({@Parameter(name = "Authorization", description = "access_token", required = true)})
    public ResponseEntity<?> deleteChatRoom(@AuthenticationPrincipal User user,
                                            @PathVariable Long roomId) {
        ResultResponseDto resultResponseDto = chatRoomService.deleteChatRoom(user, roomId);
        log.info("[ChatRoom 삭제] User: {}, chatRoomId: {}", user, roomId);
        return ResponseEntity.status(HttpStatus.OK).body(resultResponseDto);
    }


}
