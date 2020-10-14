### 프로젝트 설정
- JDK 11
- Gradle
- 스프링부트
- JPA, Querydsl
- Thymeleaf
- docker 를 이용해 PostgreSQL 사용
>윈도우 환경 Docker Toolbox 접속 방법
```shell script
docker run -p 5432:5432 -e POSTGRES_USER=woo -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=community --name postgres_spring -d postgres
docker exec -i -t postgres_spring bash
psql --username woo --dbname community
```

