package com.irlix.irlixbook.controllerV1;

import com.irlix.irlixbook.config.security.annotation.RoleAndPermissionCheck;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import com.irlix.irlixbook.dao.model.chat.request.ChatRequest;
import com.irlix.irlixbook.dao.model.chat.request.LocalMessageRequest;
import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;
import com.irlix.irlixbook.service.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final MessageService messageService;

    // ================================================================================ CHAT METHODS

    @GetMapping("/all")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<ChatOutput> getAllChats() {
        return messageService.getChats();
    }

    @PostMapping("/create")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public ChatOutput createChat(ChatRequest chatRequest) {
        return messageService.createChat(chatRequest);

    }

    @DeleteMapping("/{id}")
    @RoleAndPermissionCheck(RoleEnum.ADMIN)
    public void deleteChat(@PathVariable("id") UUID id) {
        messageService.deleteChat(id);
    }

    // ================================================================================ MESSAGES METHODS

    @GetMapping("/messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public List<MessageOutput> getLastMessages(@PathVariable("chatId") UUID chatId,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size)
    {
        List<MessageOutput> list = messageService.getLastMessages(chatId, page, size);
        return list;
    }

    @PostMapping("/messages/{chatId}")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public MessageOutput sendMessage(@Nullable @PathVariable("chatId") UUID chatId, LocalMessageRequest localMessageRequest) {
        return messageService.send(chatId, localMessageRequest);
    }

    @PutMapping("/messages")
    @RoleAndPermissionCheck(RoleEnum.USER)
    public MessageOutput updateMessage(MessageRequest messageRequest) {
        return messageService.update(messageRequest);
    }

}
