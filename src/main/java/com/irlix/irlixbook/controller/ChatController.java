package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final MessageService messageService;

    @GetMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ChatOutput getAll() {
        return chatService.getChats();
    }

    @GetMapping("messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity getLastMessages(@PathVariable("chatId") UUID chatId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size)
    {
        List<ContentResponse> list = messageService.getLastMessages(page, size);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public MessageOutput sendMessage(@PathVariable("localId") UUID localId) {
        return messageService.send(localId);
    }

    @PutMapping("messages/{id}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public void update(@PathVariable("id") UUID id) {
        messageService.update(id);
    }

}
