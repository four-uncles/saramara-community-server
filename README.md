# saramara-community-server
무언가를 구매할 때 사야할지 말아야할지 고민되는 것들을 공유하며 구매와 관련된 고민들을 덜어낼 수 있는 커뮤니티 프로젝트입니다.

---

### Project packaging
```
com.kakao.saramaracommunity
 ┣ 📂config
 ┣ 📂constant       
 ┣ 📂member
 ┃ ┣ 📂entity
 ┃ ┣ ┣ 📃Member
 ┃ ┣ 📂dto
 ┃ ┣ 📂service
 ┃ ┣ 📂repository
 ┃ ┣ ┣ 📃MemberRepository
 ┃ ┗ 📂controller
```

---

### Security with OAuth structure
Spring Security와 OAuth를 이용해 사용자 관련 보안 기능 및 소셜 로그인 기능을 구현하였습니다.
#### OAuth 도입 의도
OAuth를 도입한 의도는 아래와 같은 목록의 것들을 구글이나 카카오, 네이버 등에게 맡기고 서비스 개발에 집중하기 위함입니다.
- 로그인 시 보안
- 회원가입 시 이메일 또는 전화번호 인증
- 비밀번호 찾기, 변경
- 회원정보 변경

#### WebSecurityConfigurerAdapter Deprecated 이슈
[공식문서](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)를 보면 Spring Security 5.7.0 버전부터 WebSecurityConfigurerAdapter를 지원하지 않는다고 한다.


