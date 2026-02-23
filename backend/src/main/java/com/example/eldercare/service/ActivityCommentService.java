package com.example.eldercare.service;

import com.example.eldercare.dto.ActivityCommentDTO;
import com.example.eldercare.entity.ActivityComment;
import com.example.eldercare.entity.ActivityPost;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.ActivityCommentRepository;
import com.example.eldercare.repository.ActivityPostRepository;
import com.example.eldercare.repository.ElderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCommentService {

    private final ActivityCommentRepository activityCommentRepository;
    private final ActivityPostRepository activityPostRepository;
    private final ElderRepository elderRepository;

    public List<ActivityCommentDTO> getCommentsByPostId(Long postId) {
        List<ActivityComment> parentComments = activityCommentRepository.findByPostIdAndParentIdIsNullOrderByCreateTimeAsc(postId);

        List<ActivityCommentDTO> result = new ArrayList<>();
        for (ActivityComment parent : parentComments) {
            ActivityCommentDTO parentDTO = toDTO(parent);

            List<ActivityComment> replies = activityCommentRepository.findByParentIdOrderByCreateTimeAsc(parent.getId());
            List<ActivityCommentDTO> replyDTOs = replies.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            parentDTO.setReplies(replyDTOs);
            result.add(parentDTO);
        }

        return result;
    }

    public Page<ActivityCommentDTO> getComments(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ActivityComment> commentPage = activityCommentRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
        return commentPage.map(this::toDTO);
    }

    @Transactional
    public ActivityCommentDTO createComment(ActivityCommentDTO dto, Long currentUserId, Long currentElderId) {
        ActivityPost post = activityPostRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        ActivityComment comment = new ActivityComment();
        comment.setPostId(dto.getPostId());
        comment.setParentId(dto.getParentId());
        comment.setUserId(currentUserId);
        comment.setUserName("用户" + currentUserId);
        comment.setElderId(currentElderId);
        comment.setElderName(currentElderId != null ? "老人" + currentElderId : null);
        comment.setContent(dto.getContent());
        comment.setReplyToUserId(dto.getReplyToUserId());
        comment.setReplyToUserName(dto.getReplyToUserName());
        comment.setStatus(1);

        ActivityComment saved = activityCommentRepository.save(comment);

        post.setCommentCount((post.getCommentCount() == null ? 0 : post.getCommentCount()) + 1);
        activityPostRepository.save(post);

        return toDTO(saved);
    }

    @Transactional
    public void deleteComment(Long id) {
        ActivityComment comment = activityCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        Long postId = comment.getPostId();
        activityCommentRepository.deleteById(id);

        ActivityPost post = activityPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        Long commentCount = activityCommentRepository.countByPostId(postId);
        post.setCommentCount(commentCount.intValue());
        activityPostRepository.save(post);
    }

    public Map<String, Object> getCommentSummary(Long elderId) {
        List<ActivityComment> comments = activityCommentRepository.findByUserIdOrderByCreateTimeDesc(elderId);

        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalComments", comments.size());
        summary.put("recentComments", comments.stream().limit(5).map(this::toDTO).collect(Collectors.toList()));

        return summary;
    }

    private ActivityCommentDTO toDTO(ActivityComment comment) {
        ActivityCommentDTO dto = new ActivityCommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setParentId(comment.getParentId());
        dto.setUserId(comment.getUserId());
        dto.setUserName(comment.getUserName());
        dto.setUserAvatar(comment.getUserAvatar());
        dto.setElderId(comment.getElderId());
        dto.setElderName(comment.getElderName());
        dto.setContent(comment.getContent());
        dto.setReplyToUserId(comment.getReplyToUserId());
        dto.setReplyToUserName(comment.getReplyToUserName());
        dto.setLikeCount(comment.getLikeCount());
        dto.setStatus(comment.getStatus());
        dto.setCreateTime(comment.getCreateTime());
        return dto;
    }
}
