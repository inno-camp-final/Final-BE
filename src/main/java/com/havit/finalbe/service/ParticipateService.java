package com.havit.finalbe.service;

import com.havit.finalbe.dto.response.CertifyResponseDto;
import com.havit.finalbe.dto.response.GroupResponseDto;
import com.havit.finalbe.entity.Certify;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.entity.Participate;
import com.havit.finalbe.exception.CustomException;
import com.havit.finalbe.exception.ErrorCode;
import com.havit.finalbe.repository.CertifyRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import com.havit.finalbe.security.userDetail.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ParticipateService {

    private final ParticipateRepository participateRepository;
    private final CertifyRepository certifyRepository;
    private final GroupService groupService;
    private final ServiceUtil serviceUtil;

    @Transactional
    public GroupResponseDto participate(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(groupId);
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_PARTICIPATION);
        }

        Participate participate = Participate.builder()
                .groups(groups)
                .member(member)
                .build();
        participateRepository.save(participate);

        // 태그 가져오기
        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        // 참여한 멤버 카운팅
        int memberCount = participateRepository.countByGroups_GroupId(groupId);

        // 참여 멤버 목록
        List<Participate> participateList = participateRepository.findAllByGroups(groups);
        List<Member> memberList = new ArrayList<>();
        for (Participate participateMember : participateList) {
            memberList.add(participateMember.getMember());
        }

        // 인증샷 목록 가져오기
        List<Certify> certifyList = certifyRepository.findByGroups_GroupIdOrderByCreatedAtDesc(groupId);
        List<CertifyResponseDto> certifyResponseDtoList = new ArrayList<>();

        for (Certify certify : certifyList) {
            certifyResponseDtoList.add(
                    CertifyResponseDto.builder()
                            .certifyId(certify.getCertifyId())
                            .groupId(certify.getGroups().getGroupId())
                            .title(certify.getTitle())
                            .imageId(certify.getImageId())
                            .longitude(certify.getLongitude())
                            .latitude(certify.getLatitude())
                            .memberId(certify.getMember().getMemberId())
                            .nickname(certify.getMember().getNickname())
                            .profileImageId(certify.getMember().getImageId())
                            .createdAt(certify.getCreatedAt())
                            .modifiedAt(certify.getModifiedAt())
                            .build()
            );
        }

        return GroupResponseDto.builder()
                .groupId(groupId)
                .title(groups.getTitle())
                .writer(groups.getMember())
                .leaderName(groups.getLeaderName())
                .crewName(groups.getCrewName())
                .content(groups.getContent())
                .imageId(groups.getImageId())
                .createdAt(groups.getCreatedAt())
                .modifiedAt(groups.getModifiedAt())
                .groupTag(tagListByGroup)
                .memberCount(memberCount)
                .memberList(memberList)
                .certifyList(certifyResponseDtoList)
                .build();
    }

    @Transactional
    public GroupResponseDto cancelParticipation(Long groupId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Groups groups = groupService.isPresentGroup(groupId);
        if (null == groups) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        if (participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId()).isEmpty()) {
            throw new CustomException(ErrorCode.PARTICIPATION_NOT_FOUND);

        }

        Optional<Participate> participation = participateRepository.findByGroups_GroupIdAndMember_MemberId(groups.getGroupId(), member.getMemberId());
        Long participateId = participation.get().getParticipateId();

        Participate participate = Participate.builder()
                .participateId(participateId)
                .groups(groups)
                .member(member)
                .build();
        participateRepository.delete(participate);

        // 태그 가져오기
        List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);

        // 참여한 멤버 카운팅
        int memberCount = participateRepository.countByGroups_GroupId(groupId);

        // 참여 멤버 목록
        List<Participate> participateList = participateRepository.findAllByGroups(groups);
        List<Member> memberList = new ArrayList<>();
        for (Participate participateMember : participateList) {
            memberList.add(participateMember.getMember());
        }

        // 인증샷 목록 가져오기
        List<Certify> certifyList = certifyRepository.findByGroups_GroupIdOrderByCreatedAtDesc(groupId);
        List<CertifyResponseDto> certifyResponseDtoList = new ArrayList<>();

        for (Certify certify : certifyList) {
            certifyResponseDtoList.add(
                    CertifyResponseDto.builder()
                            .certifyId(certify.getCertifyId())
                            .groupId(certify.getGroups().getGroupId())
                            .title(certify.getTitle())
                            .imageId(certify.getImageId())
                            .longitude(certify.getLongitude())
                            .latitude(certify.getLatitude())
                            .memberId(certify.getMember().getMemberId())
                            .nickname(certify.getMember().getNickname())
                            .profileImageId(certify.getMember().getImageId())
                            .createdAt(certify.getCreatedAt())
                            .modifiedAt(certify.getModifiedAt())
                            .build()
            );
        }

        return GroupResponseDto.builder()
                .groupId(groupId)
                .title(groups.getTitle())
                .writer(groups.getMember())
                .leaderName(groups.getLeaderName())
                .crewName(groups.getCrewName())
                .content(groups.getContent())
                .imageId(groups.getImageId())
                .createdAt(groups.getCreatedAt())
                .modifiedAt(groups.getModifiedAt())
                .groupTag(tagListByGroup)
                .memberCount(memberCount)
                .memberList(memberList)
                .certifyList(certifyResponseDtoList)
                .build();
    }
}
