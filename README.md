# 📞 Curtaincall - 전화번호 보안 어플리케이션

## 👥 팀원

| [문한결](https://github.com/Munhangyeol) | [gksma](https://github.com/gksma) |
|-------------------------------------------|-------------------------------------------|
| <div align="center"><img src="https://github.com/Munhangyeol.png" width="100"></div> | <div align="center"><img src="https://github.com/gksma.png" width="100"></div> |
| **Team Leader & Spring Backend Developer** | **Frontend Developer** |

## 📝 개요
**Curtaincall**은 (주) LK한성으로부터 위탁받아 제작한 전화번호 보안 어플리케이션입니다. 사용자의 전화번호를 안전하게 보호하기 위해 **JWT**, **AES 암호화**, 그리고 **Redis**를 활용하여 높은 보안성을 제공합니다. **Spring Boot**와 **Flutter**를 기반으로 안정적인 백엔드와 직관적인 프론트엔드를 구현하였으며, **AWS** 인프라를 이용한 클라우드 배포 및 **CI/CD** 파이프라인 구축으로 효율적인 개발 및 운영 환경을 유지하고 있습니다.

현재 **구글 플레이 스토어 테스터 심사 중**이며, 곧 정식 출시 예정입니다.

## 🚀 기술 스택

- **Backend**: `Java 17`, `Spring Framework`, `Spring Boot`, `JPA`, `QueryDsl`, `Spring Security`, `JWT`, `Redis`
- **Database**: `MySQL`, `RDS`
- **Frontend**: `Flutter`
- **Infrastructure**: `AWS EC2`, `Docker`, `GitHub Actions`, `CI/CD`
- **Testing**: `Junit5`

## ✨ 주요 기능

1. **전화번호 인증 API**  
   - 사용자가 입력한 인증번호를 `Redis`에 임시로 저장하여 빠르고 안전한 인증 처리를 제공.
   - 인증번호 유효 기간 설정 및 자동 만료로 보안 강화.

2. **JWT 기반 사용자 인증 및 권한 관리**  
   - `Spring Security`와 `JWT`를 활용하여 안전한 사용자 인증 및 세션 관리 구현.
   - 사용자 역할 기반 권한 부여로 접근 제어 강화 ([🔗 고민한 글](https://velog.io/@msw0909/jwtspring-security%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%9D%B8%EC%A6%9D-%EC%B2%98%EB%A6%AC)).

3. **AES 대칭 키 암호화로 전화번호 보호**  
   - 민감한 사용자 데이터를 **AES 대칭 키 암호화**를 통해 안전하게 저장 및 관리.
   - 데이터 복호화 시 보안 키 관리를 통해 불법 접근 방지.

4. **동적 쿼리 및 데이터베이스 성능 최적화**  
   - `QueryDsl`과 `JPQL`을 활용하여 복잡한 검색 조건을 효율적으로 처리.

5. **AWS 기반 배포 및 자동화**  
   - `Docker`를 이용한 **AWS EC2** 배포로 높은 가용성 확보.
   - `GitHub Actions` 기반 **CI/CD 파이프라인** 구축으로 코드 변경 시 자동 빌드 및 배포.
   - `RDS` 및 `Redis`와 안정적인 연동으로 데이터 일관성 유지.

## 🛠 프로젝트 구조

```plaintext
.
└── curtaincall
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com.example.curtaincall
    │   │   │       ├── ControllerAdvise.java
    │   │   │       ├── CurtaincallApplication.java
    │   │   │       ├── controller
    │   │   │       │   ├── AuthorizationController.java
    │   │   │       │   ├── CallController.java
    │   │   │       │   └── UserController.java
    │   │   │       ├── domain
    │   │   │       │   ├── PhoneBook.java
    │   │   │       │   ├── RecentCallLog.java
    │   │   │       │   ├── User.java
    │   │   │       │   └── UserListener.java
    │   │   │       ├── dto
    │   │   │       │   ├── Contact.java
    │   │   │       │   ├── LogMessage.java
    │   │   │       │   ├── request
    │   │   │       │   │   ├── RequestRecentCallLogDTO.java
    │   │   │       │   │   ├── RequestRemovedNumberInPhoneBookDTO.java
    │   │   │       │   │   └── RequestUserDTO.java
    │   │   │       │   └── response
    │   │   │       │       ├── ResponseAuthorizationDTO.java
    │   │   │       │       ├── ResponsePhoneBookDTO.java
    │   │   │       │       ├── ResponseRecentCallLogDTO.java
    │   │   │       │       └── ResponseUserDTO.java
    │   │   │       ├── global
    │   │   │       │   ├── PhoneNumberConverter.java
    │   │   │       │   ├── SecretkeyManager.java
    │   │   │       │   ├── aop
    │   │   │       │   │   ├── TimeTrace.java
    │   │   │       │   │   └── TimeTraceAspect.java
    │   │   │       │   ├── auth
    │   │   │       │   │   ├── AuthorizationProvider.java
    │   │   │       │   │   ├── CurtaincallUserInfo.java
    │   │   │       │   │   ├── CustomAccessDeniedHandler.java
    │   │   │       │   │   ├── CustomAuthenticationEntryPoint.java
    │   │   │       │   │   ├── filter
    │   │   │       │   │   │   └── JwtAuthFilter.java
    │   │   │       │   │   └── jwt
    │   │   │       │   │       └── JwtUtils.java
    │   │   │       │   ├── config
    │   │   │       │   │   ├── RedisConfig.java
    │   │   │       │   │   ├── SecurityConfig.java
    │   │   │       │   │   └── WebConfig.java
    │   │   │       │   ├── dataloader
    │   │   │       │   │   ├── PhoneBookDataLoader.java
    │   │   │       │   │   ├── TestDataLoader.java
    │   │   │       │   │   └── UserDataLoader.java
    │   │   │       │   ├── exception
    │   │   │       │   │   ├── AuthorizationException.java
    │   │   │       │   │   ├── EncryptException.java
    │   │   │       │   │   ├── PhoneBookNotfoundException.java
    │   │   │       │   │   ├── UserAlreadyExistsException.java
    │   │   │       │   │   └── UserNotfoundException.java
    │   │   │       │   └── userDetail
    │   │   │       │       ├── CustomUserDetails.java
    │   │   │       │       └── CustomUserDetailService.java
    │   │   │       ├── repository
    │   │   │       │   ├── PhoneBookRepository.java
    │   │   │       │   ├── RecentCallLogRepository.java
    │   │   │       │   └── UserRepository.java
    │   │   │       └── service
    │   │   │           ├── AuthorizaionService.java
    │   │   │           ├── CallLogService.java
    │   │   │           ├── CurtainCallMessageService.java
    │   │   │           ├── PhoneBookService.java
    │   │   │           └── UserService.java
    │   │   └── resources
    │   │       ├── application.yml       # 애플리케이션 환경 설정 파일
    │   │       └── static                # 정적 파일 저장소
    │   └── test
    │       └── java
    │           └── com.example.curtaincall
    │               └── service           # 서비스 계층 테스트
    ├── Dockerfile
    ├── docker-compose.yml
    └── README.md
```

## 🔄 CI/CD 파이프라인

1. **GitHub Actions**를 통해 코드 푸시 시 자동 테스트 및 빌드.
2. **Docker**로 애플리케이션 이미지를 빌드한 후 **Docker Hub**로 푸시.
3. AWS **EC2** 인스턴스에서 이미지를 자동으로 가져와 배포.
4. **RDS** 및 **Redis**와 안정적으로 연동하여 서비스 운영.

## 🛡️ 보안 전략

1. **JWT 토큰 기반 인증**  
   - `Spring Security`와 `JWT`를 활용하여 REST API 보안 강화.
   - 토큰 만료 및 갱신 기능을 통해 보안 유지.

2. **AES 암호화**  
   - 전화번호 및 민감 정보는 **AES 대칭 키 암호화**를 사용하여 안전하게 저장.
   - 키 관리 시스템을 도입하여 데이터 복호화 시 불법 접근 방지.

3. **Redis 캐싱**  
   - 인증번호를 `Redis`에 임시 저장하여 빠른 인증 처리 및 보안 유지.
   - 캐시 만료 설정으로 데이터 유출 방지.

## 🌐 배포 환경

- **AWS EC2**: 애플리케이션 서버 호스팅
- **AWS RDS**: 데이터베이스 관리
- **Docker & Docker Compose**: 컨테이너 기반 배포 환경 구축

## 📈 성능 최적화

- ```QueryDsl```,```JPQL```을 활용한 동적 쿼리 작성으로 유연한 데이터 검색 및 처리.
  - Update문 수행시 수행 시간  0.029초 → 0.014초로 절감 ([관련 PR](https://github.com/Project-CurtainCall/backend/pull/56))
- ```Redis```를 활용한 인증번호 데이터에 빠른 접근 및 TTL설정을 활용한 저장공간 확보
- `Junit5`를 통한 지속적인 테스트 및 성능 검증.

## 🔗 링크

- [Curtaincall Github](https://github.com/Project-CurtainCall/backend)
- [JWT와 Spring Security를 활용한 사용자 인증 고민 글](https://velog.io/@msw0909/jwtspring-security%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%9D%B8%EC%A6%9D-%EC%B2%98%EB%A6%AC)
- [QueryDsl 성능 최적화 이슈](https://github.com/Project-CurtainCall/backend/issues/54)

---

**Curtaincall**은 사용자 전화번호 보안에 최적화된 솔루션을 제공하는 어플리케이션으로, 안전하고 효율적인 서비스 제공을 목표로 합니다. 🚀

