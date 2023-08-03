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
	private Long member_image_id;

	@Column(nullable = false)
	@NotBlank
	private String uuid;

	@Column(nullable = false)
	@NotBlank
	private String image_name;

	@Column(nullable = false)
	@NotBlank
	private String path;

	@Builder
	public MemberImage(String uuid, String image_name, String path){
		this.uuid = uuid;
		this.image_name = image_name;
		this.path = path;
	}

	public void changeUuid(String uuid){ this.uuid = uuid; }
	public void changeImage_name(String image_name){ this.image_name = image_name; }
	public void changePath(String path){ this.path = path; }
}
