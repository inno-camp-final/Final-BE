package com.havit.finalbe.service;


import com.havit.finalbe.dto.CommentDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Comment;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.CommentRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static com.havit.finalbe.exception.ErrorMsg.*;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CertifyService certifyService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public ResponseDto<CommentDto.Response> createComment(CommentDto.Request commentRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Certify certify = certifyService.isPresentCertify(commentRequestDto.getCertifyId());
        if (null == certify) {
            return ResponseDto.fail(CERTIFY_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .member(member)
                .certify(certify)
                .content(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return ResponseDto.success(
                CommentDto.Response.builder()
                        .commentId(comment.getCommentId())
                        .certifyId(comment.getCertify().getCertifyId())
                        .nickname(comment.getMember().getNickname())
                        .profileUrl(comment.getMember().getProfileUrl())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<CommentDto.Response> updateComment(Long commentId, CommentDto.Request commentRequestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        comment.update(commentRequestDto);

        return ResponseDto.success(
                CommentDto.Response.builder()
                        .commentId(comment.getCommentId())
                        .certifyId(comment.getCertify().getCertifyId())
                        .nickname(comment.getMember().getNickname())
                        .profileUrl(comment.getMember().getProfileUrl())
                        .content(comment.getContent())
                        .dateTime(serviceUtil.getDateFormatOfComment(comment))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<String> deleteComment(Long commentId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail(COMMENT_NOT_FOUND);
        }

        if (!comment.getMember().isValidateMember(member.getMemberId())) {
            return ResponseDto.fail(MEMBER_NOT_MATCHED);
        }

        commentRepository.delete(comment);

        return ResponseDto.success("삭제가 완료되었습니다.");
    }


    // 해당 댓글 id 유무 확인
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElse(null);
    }
}
