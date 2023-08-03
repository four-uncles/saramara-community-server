package com.kakao.saramaracommunity.member.dto;

import com.kakao.saramaracommunity.member.entity.MemberImage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberImageDto {
	private String uuid;
	private String image_name;
	private String path;

	@Builder
	public MemberImageDto(String uuid, String image_name, String path){
		this.uuid = uuid;
		this.image_name = image_name;
		this.path = path;
	}

	// Entity From Dto
	public static MemberImageDto from(MemberImage memberImage){
		return MemberImageDto.builder()
			.uuid(memberImage.getUuid())
			.image_name(memberImage.getImage_name())
			.path(memberImage.getPath())
			.build();
	}
}
