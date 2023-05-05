# Lecture

### Reference Documentation
* 개발언어: OpenJDK 17.0.4
* 프레임워크 -
* [Gradle](https://docs.gradle.org)
* [Spring boot 3.0.6](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#documentation)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* RDBMS - AWS RDS MySQL

### API Document
Controller Layer 테스트 코드를 작성할 때 Spring Rest Docs 를 이용해 API Document 를 작성했습니다.
* http://localhost:8080/docs/index.html

### 데이터 설계
* **lecture**   
  강의의 기본정보를 저장하고 있는 강의 마스터 테이블  
  id는 자동으로 생성되는 primary key입니다.  
  teacher는 강의를 진행하는 강사의 이름입니다.  
  room은 강의가 진행되는 강의실의 이름입니다.  
  max_enrollment는 강의의 최대 수강 가능 인원을 나타냅니다.  
  current_enrollment는 현재 강의에 등록된 학생 수를 나타냅니다.  
  lecture_at은 강의 일시를 나타냅니다.  
  lecture_desc는 강의에 대한 설명을 나타냅니다.  
  use_flag는 해당 강의가 사용 가능한지 여부를 나타냅니다.  
  registered_at과 registered_user_id는 각각 해당 강의의 등록 일시와 등록한 사용자의 id를 나타냅니다.  
  modified_at과 modified_user_id는 각각 해당 강의의 수정 일시와 수정한 사용자의 id를 나타냅니다.  
  version은 동시성을 고려한 낙관적 잠금을 판단하기 위한 버전을 나타냅니다.


* **employee**  
  직원의 기본정보를 저장하고 있는 직원 마스터 테이블  
  ID: 직원 고유번호 - 자동증가 PK  
  employee_no: 5자리 사원번호 - UNIQUE
  use_flag는 해당 직원이 사용 가능한지 여부를 나타냅니다  
  registered_at과 registered_user_id는 각각 해당 직원의 등록 일시와 등록한 사용자의 id를 나타냅니다  
  modified_at과 modified_user_id는 각각 해당 직원의 수정 일시와 수정한 사용자의 id를 나타냅니다


* **enrolled_lecture_employee**  
  강의를 신청한 직원의 정보를 관리하는 테이블  
  id는 자동으로 생성되는 primary key입니다.  
  lecture_id는 신청한 강의 고유번호이며 **lecture** 테이블을 참조합니다.  
  employee_id는 신청한 직원 고유번호이며 **employee** 테이블을 참조합니다.  
  use_flag는 해당 신청이 사용 가능한지 여부를 나타냅니다. 취소한 경우 false 입니다.  
  registered_at과 registered_user_id는 각각 해당 신청의 등록 일시와 등록한 사용자의 id를 나타냅니다.  
  modified_at과 modified_user_id는 각각 해당 신청의 수정 일시와 수정한 사용자의 id를 나타냅니다.

### 부연 설명
* 백오피스 API 중 강연신청자 목록 API의 경우 전체 강연 목록 및 각 강연의 신청자 목록을 조회해야하는건지 특정강연의 신청자 목록을 조회해야하는건지 정확한 의도를 알 수 없었습니다. 문제의 내용에 '모든' 이라는 말이 없었고 일반적인 백오피스 구조상 강연 목록을 조회후 강연을 클릭시 강연 상세를 조회하는 구조를 많이 사용하므로 특정 강연의 고유번호를 파라미터로 받는것으로 구현하였습니다.
* 동시성 이슈는 낙관적락과 비관적락 중 낙관적락으로 처리했습니다.
* PK 생성규칙을 비즈니스에 의존하지 않는것이 좋다고 판단하여 사번(employeeNo) 를 PK 로 하지 않고 UNIQUE 로 했습니다.
* LectureController 의 강연신청 API 에서 OptimisticLockingFailureException 이 발생시 3~5회 정도 재시도 하도록 처리할지에 대해 고민했는데 우선은 사용자가 재시도 하는것으로 처리했습니다.
* 보통 백오피스와 프론트 서버를 분리하기 때문에 별도의 프로젝트로 구성할지에 대해 고민했는데 편의를 위해 하나의 프로젝트로 구성했습니다.
* 하나의 비즈니스 예외를 만들고 예외 코드로 구분할지, 각 예외를 모두 별도의 클래스로 분리할지 고민했는데 모두 별도의 클래스로 분리했습니다.
* 보기만 해도 뜻을 알 수 있는 변수명, 메소드명, 클래스명을 작명하기 위해 고민했습니다.