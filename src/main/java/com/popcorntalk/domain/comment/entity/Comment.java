package com.popcorntalk.domain.comment.entity;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String commentContent;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Enumerated(value = EnumType.STRING)
    private DeletionStatus deletionStatus;

    @Builder
    public Comment(Long id, String commentContent, Long userId, Long postId,
        DeletionStatus deletionStatus) {
        this.id = id;
        this.commentContent = commentContent;
        this.userId = userId;
        this.postId = 1L;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Comment createOf(CommentCreateRequestDto requestDto, Long userId, Long postId) {
        return Comment.builder()
            .commentContent(requestDto.getCommentContent())
            .userId(userId)
            .postId(postId)
            .build();
    }

    public static Comment updateOf(CommentUpdateRequestDto requestDto, Long userId, Long postId,
        Long commentId) {
        return Comment.builder().commentContent(requestDto.getCommentContent())
            .userId(userId)
            .postId(postId).id(commentId).build();
    }

    public void update(Comment comment) {
        this.commentContent = comment.getCommentContent();
    }


}
