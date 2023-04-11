# GreenFarm Project
[https://greenfarm-buchu.link](https://greenfarm-buchu.link)   
트위터와 유사한 일기 사이트 입니다. 주요 기능과 사용 예시는 
[블로그](https://buchu-doodle.tistory.com/160)에서 확인하실 수 있습니다.
해당 블로그 동일 카테고리 글을 보시면 프로젝트의 전반적인 구현 과정을 확인하실 수 있습니다.   

다음은 기능 구현을 위해 사용한 기술 목록입니다.

## 0. Spring Boot
Spring Boot 3.x를 이용했습니다. Spring을 이용해서 처음으로 진행해본 프로젝트입니다.
<br />

## 1. RDS + MySQL
RDB로 MySQL을 선택했고, AWS의 RDS를 이용했습니다. ORM 프레임워크로는 hibernate, Spring Data JPA를 사용했습니다.   

![ERD](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F2dtrV%2Fbtr3vOi476H%2FHtAZVk51gS7awUArpHAFVK%2Fimg.png)
ERD입니다.
유저, 유저가 작성한 일기, 유저간 팔로우 관계, 일기와 유저의 좋아요 관계가 있는
간단한 relation입니다.   
<br />

## 2. Spring Security + OAuth2
사이트의 회원가입 및 인증을 구현을 위해 spring security, oauth2 프로토콜을 이용했습니다.
현재 Google 계정을 통한 로그인만 가능한 상태입니다.
아이디와 비밀번호 기반 로그인 시스템도 구현은 했지만, 브라우저의 보안 경고 오류 메세지가 불안하고 거슬린다는
이용자의 피드백을 반영해 해당 기능은 삭제했습니다.   
Auth 부분과 관련된 소스코드들은 [auth 디렉토리](https://github.com/BuchuKim/green-farm/tree/main/src/main/java/com/buchu/greenfarm/config/auth)에서
확인하실 수 있습니다.
<br />

## 3. Querydsl
동적 쿼리의 원활한 작성을 위해 querydsl을 사용했습니다. querydsl을 사용하며 직면한
문제들과 나름의 해결 과정은 [블로그 글](https://buchu-doodle.tistory.com/178?category=1090263)에서
확인하실 수 있습니다. 실제 쿼리문이 담긴 소스코드는 [이쪽](https://github.com/BuchuKim/green-farm/blob/main/src/main/java/com/buchu/greenfarm/repository/FarmLogCustomRepositoryImpl.java)입니다.
<br />

## 4. Thymeleaf
템플릿 엔진으로는 thymeleaf를 사용하였습니다. Spring security와의 원활한 연동을 위해
thymeleaf-extras 라이브러리를 추가로 사용했습니다.
<br />

## 5. Elastic Beanstalk
프로젝트 배포에 AWS의 Elastic Beanstalk을 이용했습니다. 
<br />