package com.noreabang.strawberryrabbit.domain.comment.service

import com.noreabang.strawberryrabbit.domain.comment.dto.CommentRequest
import com.noreabang.strawberryrabbit.domain.comment.dto.CommentResponse
import com.noreabang.strawberryrabbit.domain.comment.model.Comment
import com.noreabang.strawberryrabbit.domain.comment.repository.CommentRepository
import com.noreabang.strawberryrabbit.domain.feed.repository.FeedRepository
import com.noreabang.strawberryrabbit.domain.member.repository.MemberRepository
import com.noreabang.strawberryrabbit.infra.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberRepository: MemberRepository,
    private val feedRepository: FeedRepository,
) {
    fun createComment(feedId: Long, memberId: Long?, request: CommentRequest): CommentResponse {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw ModelNotFoundException("Feed", feedId)
        val member = memberRepository.findByIdOrNull(memberId)
        return commentRepository.save(
            Comment.createComment(request, feed, member)
        ).toResponse()
    }

    fun updateComment(commentId: Long, memberId: Long?, request: CommentRequest): CommentResponse {
        val member = memberId.let { memberRepository.findByIdOrNull(it) }
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.member != member) {
            throw AccessDeniedException("This feed/comment is not yours!!")
        }

        comment.content = request.content
            return commentRepository.save(comment).toResponse()
    }

    fun deleteComment(commentId: Long, memberId: Long?) {
        val member = memberId.let { memberRepository.findByIdOrNull(it) }
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.member != member) {
            throw AccessDeniedException("This feed/comment is not yours!!")
        }
        commentRepository.delete(comment)
    }
}