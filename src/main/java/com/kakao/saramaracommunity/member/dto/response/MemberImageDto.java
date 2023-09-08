package com.kakao.saramaracommunity.member.dto.response;

import com.kakao.saramaracommunity.member.entity.MemberImage;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImageDto {

	private String uuid;
	private String image_name;
	private String path;

	@Builder
	private MemberImageDto(String uuid, String image_name, String path){
		this.uuid = uuid;
		this.image_name = image_name;
		this.path = path;
	}

	// Entity From Dto
	public static MemberImageDto from(MemberImage memberImage){
		return MemberImageDto.builder()
			.uuid(memberImage.getUuid())
			.image_name(memberImage.getImageName())
			.path(memberImage.getPath())
			.build();
	}

}
