package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.MessageDTO;
import com.example.eldercare.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public Result<PageResult<MessageDTO>> getMessages(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageDTO> result = messageService.getMessages(type, isRead, pageable);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<MessageDTO> getMessageById(@PathVariable Long id) {
        return Result.success(messageService.getMessageById(id));
    }

    @PostMapping("/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success();
    }

    @PostMapping("/read-all")
    public Result<Void> markAllAsRead() {
        messageService.markAllAsRead();
        return Result.success();
    }

    @GetMapping("/unread-count")
    public Result<java.util.Map<String, Long>> getUnreadCount() {
        return Result.success(messageService.getUnreadCount());
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return Result.success();
    }
}
