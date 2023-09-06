package com.kakao.saramaracommunity.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MemberImage {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberImageId;

	@Column(nullable = false)
	@NotBlank
	private String uuid;

	@Column(nullable = false)
	@NotBlank
	private String imageName;

	@Column(nullable = false)
	@NotBlank
	private String path;

	@Builder
	public MemberImage(String uuid, String imageName, String path){
		this.uuid = uuid;
		this.imageName = imageName;
		this.path = path;
	}

	public void changeUuid(String uuid){ this.uuid = uuid; }
	public void changeImage_name(String image_name){ this.imageName = image_name; }
	public void changePath(String path){ this.path = path; }
}
