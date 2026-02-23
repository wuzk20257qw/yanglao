package com.example.eldercare.service;

import com.example.eldercare.dto.ActivityPostDTO;
import com.example.eldercare.entity.ActivityComment;
import com.example.eldercare.entity.ActivityLike;
import com.example.eldercare.entity.ActivityPost;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.ActivityCommentRepository;
import com.example.eldercare.repository.ActivityLikeRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityPostService {

    private final ActivityPostRepository activityPostRepository;
    private final ActivityCommentRepository activityCommentRepository;
    private final ActivityLikeRepository activityLikeRepository;
    private final ElderRepository elderRepository;

    private static final String UPLOAD_DIR = "uploads/activities/";

    public Page<ActivityPostDTO> getPosts(
            Long elderId,
            String activityType,
            String keyword,
            Integer page,
            Integer size,
            Long currentUserId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isPinned", "createTime"));

        Page<ActivityPost> postPage;

        if (keyword != null && !keyword.isEmpty()) {
            postPage = activityPostRepository.searchByKeyword(keyword, pageable);
        } else if (activityType != null && !activityType.isEmpty()) {
            postPage = activityPostRepository.findByActivityTypeAndStatusOrderByCreateTimeDesc(activityType, 1, pageable);
        } else if (elderId != null) {
            postPage = activityPostRepository.findByElderIdAndStatusOrderByCreateTimeDesc(elderId, 1, pageable);
        } else {
            postPage = activityPostRepository.findByStatusOrderByCreateTimeDesc(1, pageable);
        }

        return postPage.map(post -> toDTO(post, currentUserId));
    }

    public ActivityPostDTO getPostById(Long id, Long currentUserId) {
        ActivityPost post = activityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        post.setViewCount((post.getViewCount() == null ? 0 : post.getViewCount()) + 1);
        activityPostRepository.save(post);

        return toDTO(post, currentUserId);
    }

    public List<ActivityPostDTO> getPinnedPosts(Long currentUserId) {
        List<ActivityPost> posts = activityPostRepository.findPinnedPosts();
        return posts.stream().map(post -> toDTO(post, currentUserId)).collect(Collectors.toList());
    }

    public List<ActivityPostDTO> getHotPosts(Long currentUserId) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        List<ActivityPost> posts = activityPostRepository.findHotPosts(startTime);
        return posts.stream().limit(10).map(post -> toDTO(post, currentUserId)).collect(Collectors.toList());
    }

    @Transactional
    public ActivityPostDTO createPost(ActivityPostDTO dto, List<MultipartFile> imageFiles, List<MultipartFile> videoFiles, Long currentUserId) throws IOException {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new RuntimeException("老人档案不存在"));

        ActivityPost post = new ActivityPost();
        post.setElderId(dto.getElderId());
        post.setElderName(elder.getName());
        post.setActivityType(dto.getActivityType());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setLocation(dto.getLocation());
        post.setParticipantCount(dto.getParticipantCount());
        post.setActivityDate(dto.getActivityDate() != null ? dto.getActivityDate() : LocalDateTime.now());
        post.setTags(dto.getTags() != null ? String.join(",", dto.getTags()) : null);
        post.setStatus(1);

        List<String> imageUrls = new ArrayList<>();
        List<String> videoUrls = new ArrayList<>();

        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String url = saveFile(file, "images");
                    imageUrls.add(url);
                }
            }
        }

        if (videoFiles != null) {
            for (MultipartFile file : videoFiles) {
                if (!file.isEmpty()) {
                    String url = saveFile(file, "videos");
                    videoUrls.add(url);
                }
            }
        }

        post.setImages(imageUrls.isEmpty() ? null : String.join(",", imageUrls));
        post.setVideos(videoUrls.isEmpty() ? null : String.join(",", videoUrls));

        ActivityPost saved = activityPostRepository.save(post);
        return toDTO(saved, currentUserId);
    }

    @Transactional
    public ActivityPostDTO updatePost(Long id, ActivityPostDTO dto, List<MultipartFile> imageFiles, List<MultipartFile> videoFiles, Long currentUserId) throws IOException {
        ActivityPost post = activityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getLocation() != null) {
            post.setLocation(dto.getLocation());
        }
        if (dto.getParticipantCount() != null) {
            post.setParticipantCount(dto.getParticipantCount());
        }
        if (dto.getActivityType() != null) {
            post.setActivityType(dto.getActivityType());
        }
        if (dto.getActivityDate() != null) {
            post.setActivityDate(dto.getActivityDate());
        }
        if (dto.getTags() != null) {
            post.setTags(String.join(",", dto.getTags()));
        }
        if (dto.getIsPinned() != null) {
            post.setIsPinned(dto.getIsPinned());
        }

        List<String> existingImages = post.getImages() != null ?
            new ArrayList<>(Arrays.asList(post.getImages().split(","))) : new ArrayList<>();
        List<String> existingVideos = post.getVideos() != null ?
            new ArrayList<>(Arrays.asList(post.getVideos().split(","))) : new ArrayList<>();

        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String url = saveFile(file, "images");
                    existingImages.add(url);
                }
            }
        }

        if (videoFiles != null) {
            for (MultipartFile file : videoFiles) {
                if (!file.isEmpty()) {
                    String url = saveFile(file, "videos");
                    existingVideos.add(url);
                }
            }
        }

        post.setImages(existingImages.isEmpty() ? null : String.join(",", existingImages));
        post.setVideos(existingVideos.isEmpty() ? null : String.join(",", existingVideos));

        ActivityPost saved = activityPostRepository.save(post);
        return toDTO(saved, currentUserId);
    }

    @Transactional
    public void deletePost(Long id) {
        ActivityPost post = activityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        if (post.getImages() != null) {
            for (String imageUrl : post.getImages().split(",")) {
                deleteFile(imageUrl);
            }
        }

        if (post.getVideos() != null) {
            for (String videoUrl : post.getVideos().split(",")) {
                deleteFile(videoUrl);
            }
        }

        activityCommentRepository.deleteByPostId(id);
        activityLikeRepository.deleteByPostId(id);
        activityPostRepository.deleteById(id);
    }

    @Transactional
    public void likePost(Long postId, Long userId, Long elderId) {
        Optional<ActivityLike> existingLike = activityLikeRepository.findByPostIdAndUserId(postId, userId);

        ActivityPost post = activityPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        if (existingLike.isPresent()) {
            activityLikeRepository.deleteByPostIdAndUserId(postId, userId);
            post.setLikeCount(Math.max(0, (post.getLikeCount() == null ? 0 : post.getLikeCount()) - 1));
        } else {
            ActivityLike like = new ActivityLike();
            like.setPostId(postId);
            like.setUserId(userId);
            like.setUserName("用户" + userId);
            like.setElderId(elderId);
            like.setElderName(elderId != null ? "老人" + elderId : null);
            activityLikeRepository.save(like);

            post.setLikeCount((post.getLikeCount() == null ? 0 : post.getLikeCount()) + 1);
        }

        activityPostRepository.save(post);
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIR + subDir + File.separator;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/api/files/activities/" + subDir + "/" + newFilename;
    }

    private void deleteFile(String fileUrl) {
        try {
            String pathPart = fileUrl.replace("/api/files/activities/", "");
            Path filePath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR, pathPart);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("删除文件失败: {}", fileUrl, e);
        }
    }

    private ActivityPostDTO toDTO(ActivityPost post, Long currentUserId) {
        ActivityPostDTO dto = new ActivityPostDTO();
        dto.setId(post.getId());
        dto.setElderId(post.getElderId());
        dto.setElderName(post.getElderName());
        dto.setActivityType(post.getActivityType());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        if (post.getImages() != null) {
            dto.setImages(Arrays.asList(post.getImages().split(",")));
        }
        if (post.getVideos() != null) {
            dto.setVideos(Arrays.asList(post.getVideos().split(",")));
        }

        dto.setLocation(post.getLocation());
        dto.setParticipantCount(post.getParticipantCount());
        dto.setActivityDate(post.getActivityDate());

        if (post.getTags() != null) {
            dto.setTags(Arrays.asList(post.getTags().split(",")));
        }

        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setIsPinned(post.getIsPinned());
        dto.setStatus(post.getStatus());
        dto.setCreateTime(post.getCreateTime());
        dto.setUpdateTime(post.getUpdateTime());

        if (currentUserId != null) {
            dto.setIsLiked(activityLikeRepository.findByPostIdAndUserId(post.getId(), currentUserId).isPresent());
        }

        return dto;
    }
}
