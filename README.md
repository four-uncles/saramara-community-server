# saramara-community-server
무언가를 구매할 때 사야할지 말아야할지 고민되는 것들을 공유하며 구매와 관련된 고민들을 덜어낼 수 있는 커뮤니티 프로젝트입니다.

<br><hr><br>

## Project packaging

```
com.kakao.saramaracommunity
 ┣ 📂 config
 ┣ 📂 util
 ┣ 📂 security
 ┣ 📂 domain
 ┃ ┣ 📂 entity
 ┃ ┣ 📂 dto
 ┃ ┗ 📂 controller
 ┃ ┣ 📂 service
 ┃ ┣ 📂 repository
 ┃ ┗ 📂 exception
```

<br><hr><br>

## API specification

### 기능 명세 목록

|분류|기능|담당자|개발 우선순위|완료여부|
|:--:|:--:|:--:|:--:|:--:|
|메인|- 모든 게시글 가져오기|송헌욱|3|💬|
|메인|- 모든 게시글의 이미지 가져오기|문태준|5|💬|
|로그인|- 입력된 아이디 검증하기(아이디 존재 여부와 동시에 아이디 검증, 비밀번호 틀린 경우)|이현범|2|💬|
|로그인|- 카카오 소셜 로그인하기|이현범|미정|💬|
|회원가입|- 회원가입 처리하기|이현범|1|💬|
|마이페이지|- 내 정보 수정하기|이현범|3|💬|
|마이페이지|- 회원 탈퇴하기|이현범|4|💬|
|게시글 상세 조회|- 하나의 게시글 가져오기|송헌욱|2|💬|
|게시글 상세 조회|- 해당 게시글과 댓글의 이미지 가져오기|문태준|2|💬|
|게시글 작성|- 게시글 작성하기|송헌욱|1|💬|
|게시글 작성|- 게시글 첨부 이미지 파일 등록하기|문태준|1|💬|
|게시글 수정|- 게시글 수정하기|송헌욱|4|💬|
|게시글 수정|- 게시글 첨부 이미지 파일 수정하기|문태준|3|💬|
|게시글 삭제|- 게시글 삭제하기|송헌욱|5|💬|
|게시글 삭제|- 게시글에 첨부된 이미지 삭제하기|문태준|4|💬|
|댓글 조회|- 하나의 게시글에 대한 모든 댓글 가져오기|손병주|1|💬|
|댓글 조회|- 댓글 첨부 이미지 가져오기|문태준|미정|💬|
|댓글 작성|- 하나의 게시글에 댓글 작성하기|손병주|2|💬|
|댓글 작성|- 댓글에 첨부한 이미지 파일 등록하기|문태준|미정|💬|
|댓글 수정|- 댓글 수정하기|손병주|3|💬|
|댓글 수정|- 댓글 첨부 이미지 수정하기|문태준|미정|💬|
|댓글 삭제|- 댓글 삭제하기|손병주|4|💬|
|댓글 삭제|- 댓글 첨부 이미지 삭제하기|문태준|미정|💬|

* 개발 완료 여부는 진행중(💬)과 완료(✅)로 구분

API 명세서의 경우 [노션 링크](https://www.notion.so/API-bbd684c73e4d41c3b27a5872a4717060?pvs=4)에서 자세한 내용을 확인할 수 있습니다.

<br><hr><br>

