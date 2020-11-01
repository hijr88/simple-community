### 프로젝트 설정
- JDK 11
- Gradle
- Spring Boot
- JPA, Querydsl
- Thymeleaf
- docker 를 이용해 PostgreSQL 사용
>윈도우 환경 Docker Toolbox 접속 방법
```shell script
docker run -p 5432:5432 -e POSTGRES_USER=woo -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=community --name postgres_spring -d postgres
docker exec -i -t postgres_spring bash
psql --username woo --dbname community
```
- Flyway 를 이용한 마이그레이션
---
### 프로젝트 기능
- 회원
  - 회원가입, 메일 인증
  - 로그인 유지, 중복 방지
  - 회원 정보 수정
  - 아이디, 패스워드 찾기
  - 회원 탈퇴
- 글 게시판
  - 리스트 페이징, 검색기능
  - 글 입력, 수정, 삭제, 글 답글
  - 댓글 입력, 대댓글
- 이미지 게시판
  - 리스트 페이징
  - 업로드, 수정, 삭제
- 관리자
  - 회원 리스트, 회원 등급 변경, 탈퇴 처리
 

