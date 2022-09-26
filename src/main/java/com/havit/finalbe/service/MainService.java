package com.havit.finalbe.service;

import com.havit.finalbe.dto.GroupDto;
import com.havit.finalbe.dto.response.ResponseDto;
import com.havit.finalbe.entity.Favorite;
import com.havit.finalbe.entity.Groups;
import com.havit.finalbe.entity.Member;
import com.havit.finalbe.repository.FavoriteRepository;
import com.havit.finalbe.repository.GroupRepository;
import com.havit.finalbe.repository.ParticipateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.havit.finalbe.exception.ErrorMsg.INVALID_TOKEN;

@RequiredArgsConstructor
@Service
public class MainService {

    private final GroupRepository groupRepository;
    private final ParticipateRepository participateRepository;
    private final FavoriteRepository favoriteRepository;
    private final ServiceUtil serviceUtil;

    // 그룹 통합 검색
    @Transactional
    public ResponseDto<?> searchGroup(String searchWord, HttpServletRequest request) {

        Member member = serviceUtil.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        List<Groups> groupList = groupRepository
                .findAllByTitleContainingIgnoreCaseOrMember_NicknameContainingIgnoreCase(searchWord, searchWord);
        List<GroupDto.AllGroupList> searchGroupListResponseDtoList = new ArrayList<>();

        boolean isFavorites = false;

        for (Groups groups : groupList) {
            int memberCount = participateRepository.countByGroups_GroupId(groups.getGroupId());
            List<String> tagListByGroup = serviceUtil.getTagNameListFromGroupTag(groups);
            Favorite checkFavorite = favoriteRepository
                    .findByMember_MemberIdAndGroups_GroupId(member.getMemberId(), groups.getGroupId());
            if (null != checkFavorite) {
                isFavorites = true;
            }

            // 로그인한 멤버의 계급 확인 코드 ( 만약 추가하면 AllGroupListResponseDto 에도 필드 추가해야 함 )

            GroupDto.AllGroupList allGroupListResponseDto = GroupDto.AllGroupList.builder()
                    .groupId(groups.getGroupId())
                    .title(groups.getTitle())
                    .imgUrl(groups.getImgUrl())
                    .memberCount(memberCount)
                    .groupTag(tagListByGroup)
                    .createdAt(groups.getCreatedAt())
                    .modifiedAt(groups.getModifiedAt())
                    .favorite(isFavorites)
                    .build();
            searchGroupListResponseDtoList.add(allGroupListResponseDto);
        }

        return ResponseDto.success(searchGroupListResponseDtoList);
    }
}