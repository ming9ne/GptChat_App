# Gpt 채팅 웹 어플리케이션

#### <a href="https://sw-sth.notion.site/898e9ed7b37e4d4e8170a86defd68769">노션 프로젝트 소개</a>

## 🖥️프로젝트 소개
#### Openai, MessageQueue를 활용한 시스템 간 통신 및 데이터 처리를 효율적으로 관리</br>



### 주제
Openai를 통해 여러명의 사용자가 동시에 Chat GPT-4 모델을 자유롭게 사용 및 로그 분석 서비스

### 목적
유로 모델인 GPT-4를 여려명의 사용자가 동시에 사용 가능한 서비스 개발 목적

## 🕰️개발 기간
- 24.04.08 ~ 24.05.05

### 🧑‍🤝‍🧑팀원
- 풀스택 박태현

## ⚙️개발 환경
- `Java 17` 
- `JDK 17` 
- **IDE** : Intellij 
- **Framework** : 스프링부트 3.2.4 
- **Front** : HTML
- **DataBase** : MySQL
- **ORM** : JPA
- **Release** : Docker

## 📌주요 기능
#### 회원 관리
- 관리자가 등록한 명부 정보를 기반으로 회원가입
- Spring Security를 이용한 회원가입 및 Password 관리 
- 사용자 Password 변경
![대시보드](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/923d473b-f137-4716-9804-758e640ddedf)

#### GPT 채팅
- GPT-4 모델 실시간 채팅
- 히스토리 최대 개수 사용자 설정 기능
- 최근 대화내역을 기반으로 한 채팅
- 채팅 별 사용 토큰, 전제 토큰 조절 
![지도](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/720c5076-ef84-46f6-aac0-cad0afecf86b)
#### 채팅 로그 조회
- 사용자의 최근 대화 내역 조회
- 페이징 처리
- 시간대별 대화 조회 
![리스트](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7c8a54e6-2770-42f6-880d-fb3164a611f3)
#### 학생 명부 업로드
- 관리자가 사전에 사용자 정보 등록 
- .XLS, .XLSX 형태의 파일을 업로드 시 DataBase에 업로드
- 등록된 사용자 정보만 회원가입 승인 
![디테일](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/978ea2d3-251c-4a54-b778-631bab7be144)
![디테일 지도](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7185ef78-eca5-4630-ac7c-eef01145fd3c)
#### 관리자 페이지
- 유저 정보 조회 및 삭제
- 사용자 별 최대 토큰 제한 설정
![프로필](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/178b8981-119f-44e9-97dd-6cf83b6be06d)
#### MessageQueue
- 비동기 통신을 통한 응답 시간 최적화
- 탄력성 및 확장성 보장
![쿠폰](https://github.com/kariseio/MSA_CultureWithSBike/assets/39698079/7e86381a-14d9-4999-9f7b-ba5c8040f7a2)
64efab-5717-4322-804b-0c0555c933a2)

## 💻주요 기술
- 등록된 사용자 기반으로 한 회원가입
- Spring Security 적용 
- MessageQueue를 사용해 메세지를 순서대로 처리
- 여러명의 유저가 동시에 GPT-4 채팅 사용
- 관리자가 학생 명부 업로드 및 토큰 제한 설정
