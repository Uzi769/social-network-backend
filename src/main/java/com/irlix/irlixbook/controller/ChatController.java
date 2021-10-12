package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.chat.request.LocalMessageRequest;
import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final MessageService messageService;

    @GetMapping
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ChatOutput> getAll() {
        return messageService.getChats();
    }

    @GetMapping("messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public ResponseEntity getLastMessages(@PathVariable("chatId") UUID chatId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size)
    {
        List<MessageOutput> list = messageService.getLastMessages(chatId, page, size);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public MessageOutput sendMessage(@Nullable @PathVariable("chatId") UUID chatId, LocalMessageRequest localMessageRequest) {
        return messageService.send(chatId, localMessageRequest);
    }

    @PutMapping("messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public MessageOutput update(@PathVariable("chatId") UUID chatId, MessageRequest messageRequest) {
        return messageService.update(chatId, messageRequest);
    }

}
