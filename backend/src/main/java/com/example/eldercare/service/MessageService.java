package com.example.eldercare.service;

import com.example.eldercare.dto.MessageDTO;
import com.example.eldercare.entity.Message;
import com.example.eldercare.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Page<MessageDTO> getMessages(String type, Boolean isRead, Pageable pageable) {
        // 暂时使用 userId = 1，实际应从 token 获取
        Long userId = 1L;
        Page<Message> messages;

        if (type != null && isRead != null) {
            messages = messageRepository.findByUserIdAndTypeOrderByCreateTimeDesc(userId, type, pageable);
        } else if (type != null) {
            messages = messageRepository.findByUserIdAndTypeOrderByCreateTimeDesc(userId, type, pageable);
        } else if (isRead != null) {
            messages = messageRepository.findByUserIdAndIsReadOrderByCreateTimeDesc(userId, isRead, pageable);
        } else {
            messages = messageRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
        }

        return messages.map(this::toDTO);
    }

    public MessageDTO getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        return toDTO(message);
    }

    @Transactional
    public void markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setIsRead(true);
        message.setReadTime(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Transactional
    public void markAllAsRead() {
        Long userId = 1L;
        List<Message> unreadMessages = messageRepository.findByUserIdAndIsReadOrderByCreateTimeDesc(userId, false, Pageable.unpaged()).getContent();
        unreadMessages.forEach(msg -> {
            msg.setIsRead(true);
            msg.setReadTime(LocalDateTime.now());
        });
        messageRepository.saveAll(unreadMessages);
    }

    public Map<String, Long> getUnreadCount() {
        Long userId = 1L;
        Map<String, Long> counts = new HashMap<>();
        counts.put("total", messageRepository.countByUserIdAndIsRead(userId, false));
        
        List<Object[]> typeCounts = messageRepository.countUnreadByType(userId);
        for (Object[] row : typeCounts) {
            counts.put((String) row[0], (Long) row[1]);
        }
        return counts;
    }

    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    private MessageDTO toDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setUserId(message.getUserId());
        dto.setType(message.getType());
        dto.setTitle(message.getTitle());
        dto.setContent(message.getContent());
        dto.setIsRead(message.getIsRead());
        dto.setRelatedType(message.getRelatedType());
        dto.setRelatedId(message.getRelatedId());
        dto.setCreateTime(message.getCreateTime());
        dto.setReadTime(message.getReadTime());
        return dto;
    }
}
