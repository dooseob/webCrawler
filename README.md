## 요양기관 평가정보 크롤러 (LongTermCareScraper)

### 국민건강보험공단 노인장기요양보험 웹사이트에서 요양기관 목록 및 상세 평가 정보를 자동 수집하는 Java 기반 웹 크롤러

-----

### 목차

  - [프로젝트 소개]
  - [주요 기능]
  - [수집 데이터 항목]
  - [기술 스택]
  - [설치 및 실행 방법]
  - [코드 설명]
  - [로드맵]
  - [라이선스]
  - [문의]

-----

### 프로젝트 소개

이 프로젝트는 **국민건강보험공단 노인장기요양보험 웹사이트**에서 전국의 요양기관 목록과 각 기관의 상세 평가 정보를 자동으로 수집하기 위해 개발된 Java 기반 웹 크롤러입니다. Selenium WebDriver를 활용하여 동적으로 로드되는 웹 페이지의 데이터를 안정적으로 추출하며, 페이지네이션 처리 및 상세 페이지 탐색 기능을 포함하고 있습니다.

**주요 목적:**

  * **요양기관 정보 자동 수집:** 웹사이트에서 수동으로 확인해야 하는 복잡한 요양기관 데이터를 자동으로 모읍니다.
  * **데이터 기반 의사결정 지원:** 수집된 기관 평가 정보를 통해 요양기관 선택에 필요한 객관적인 데이터를 제공합니다.
  * **데이터 분석을 위한 기반 마련:** 수집된 정량적/정성적 데이터를 활용하여 추가적인 분석이나 통계 자료 작성의 기초 자료로 활용될 수 있습니다.

-----

### 주요 기능

  * **동적 웹 페이지 크롤링:** Selenium WebDriver를 사용하여 JavaScript로 렌더링되는 페이지 (예: 목록 검색 탭, 상세 평가 정보 탭)의 데이터를 성공적으로 추출합니다.
  * **페이지네이션 처리:** 전체 페이지 수를 자동으로 파악하고, 각 페이지를 순회하며 모든 요양기관 정보를 수집합니다.
  * **상세 평가 정보 추출:** 각 요양기관의 상세 페이지로 이동하여 평가 등급, 총점 및 항목별 점수(기관운영, 환경 및 안전, 수급자 권리 보장, 급여 제공 과정, 급여 제공 결과)를 추출합니다.
  * **데이터 구조화:** `FacilityEvaluationDTO`를 사용하여 수집된 데이터를 체계적으로 저장하고 관리합니다.
  * **예외 처리 및 안정성:** `StaleElementReferenceException`과 같은 Selenium 관련 일반적인 오류를 방지하기 위한 로직이 적용되어 안정적인 크롤링을 수행합니다.
  * **브라우저 제어 옵션:** `--headless` 모드 지원, `--disable-gpu`, `--remote-allow-origins=*`, `--lang=ko` 등 다양한 Chrome 옵션을 설정하여 크롤링 환경을 최적화할 수 있습니다.

-----

### 수집 데이터 항목

`FacilityEvaluationDTO` 클래스를 통해 수집되는 주요 데이터 항목은 다음과 같습니다.

#### 1\. 목록 정보

  * **`facilityName`**: 요양기관 이름
  * **`facilityType`**: 급여 종류 (예: 노인요양시설, 주야간보호)
  * **`summaryEvaluation`**: 평가결과 요약 (예: 최우수기관, 우수기관)
  * **`capacity`**: 정원
  * **`currentOccupancy`**: 현원
  * **`remainingCapacity`**: 잔여 정원
  * **`waitingCount`**: 대기 인원
  * **`bathVehicle`**: 목욕차량 유무
  * **`address`**: 주소
  * **`phoneNumber`**: 전화번호
  * **`operationHours`**: 운영 시간

#### 2\. 상세 평가 정보

  * **`evaluationGrade`**: 상세 평가 등급 (예: A등급, B등급)
  * **`totalScore`**: 총점
  * **`managementScore`**: 기관운영 점수
  * **`environmentScore`**: 환경 및 안전 점수
  * **`rightsScore`**: 수급자 권리 보장 점수
  * **`processScore`**: 급여 제공 과정 점수
  * **`resultScore`**: 급여 제공 결과 점수

-----

### 기술 스택

  *  (JDK 11 이상 권장)
  *  WebDriver
  *  (및 ChromeDriver)

-----

### 설치 및 실행 방법

1.  **사전 준비물:**

      * **Java Development Kit (JDK) 11 이상**이 설치되어 있어야 합니다.
      * **Google Chrome 브라우저**가 설치되어 있어야 합니다.
      * **ChromeDriver**: 설치된 Chrome 브라우저 버전에 맞는 ChromeDriver를 다운로드하여 실행 파일 경로를 `LongTermCareScraper.java` 파일의 `CHROME_DRIVER_PATH` 변수에 맞게 수정해야 합니다.
          * ChromeDriver 다운로드: [https://chromedriver.chromium.org/downloads](https://chromedriver.chromium.org/downloads)

2.  **프로젝트 클론:**

    ```bash
    git clone https://github.com/dooseob/webCrawler.git
    cd webCrawler
    ```

3.  **프로젝트 설정 (IDE: Eclipse 또는 IntelliJ IDEA 권장):**

      * 해당 프로젝트를 IDE로 Import 합니다. (Maven 또는 Gradle 프로젝트로 인식시켜 의존성 관리)
      * `pom.xml` 또는 `build.gradle`에 Selenium WebDriver 종속성을 추가해야 합니다. (아래 예시 참고)

    <!-- end list -->

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.21.0</version> </dependency>
    </dependencies>
    ```

4.  **`CHROME_DRIVER_PATH` 설정:**

      * `src/main/java/com/web/crawler/LongTermCareScraper.java` 파일을 열어 `CHROME_DRIVER_PATH` 변수 값을 본인의 ChromeDriver 실행 파일 경로에 맞게 수정합니다.
        ```java
        private static final String CHROME_DRIVER_PATH = "C:/chromedriver/chromedriver.exe"; // YOUR_PATH_TO_CHROMEDRIVER
        ```

5.  **실행:**

      * IDE에서 `LongTermCareScraper.java` 파일을 찾아 `main` 메소드를 실행합니다.
      * 콘솔에 크롤링 진행 상황과 추출된 데이터가 출력됩니다.

-----

### 코드 설명

  * **`LongTermCareScraper.java`**:

      * Selenium WebDriver를 초기화하고 웹 페이지에 접속합니다.
      * '목록검색' 탭을 클릭하여 요양기관 목록을 로드합니다.
      * 총 페이지 수를 파악하여 모든 페이지를 순회합니다.
      * 각 페이지에서 요양기관 목록의 기본 정보(이름, 주소 등)를 미리 추출합니다.
      * 각 기관의 상세 페이지로 이동하여 '평가정보' 탭을 클릭한 후, 상세 평가 등급 및 항목별 점수를 추출합니다.
      * 추출된 모든 정보를 `FacilityEvaluationDTO` 객체에 담아 리스트에 추가합니다.
      * `StaleElementReferenceException`과 같은 동적 웹페이지 로딩 문제를 해결하기 위해, 페이지 이동 후 요소를 다시 찾는 로직이 적용되어 있습니다.
      * 크롤링이 완료되면 WebDriver를 종료합니다.

  * **`FacilityEvaluationDTO.java`**:

      * 수집할 요양기관 정보(목록 정보, 상세 평가 정보)를 정의하는 데이터 전송 객체(DTO)입니다.
      * 각 필드에 대한 Getter/Setter 메소드와 `toString()` 메소드가 구현되어 있어 데이터 관리가 용이합니다.

-----

### 로드맵

  * [ ] 수집된 데이터를 CSV, JSON, 또는 데이터베이스(MySQL 등)에 저장하는 기능 추가
  * [ ] 특정 지역(시/도, 시/군/구)을 매개변수로 받아 크롤링하도록 기능 확장
  * [ ] 크롤링 진행 상황을 UI로 보여주는 데스크톱 애플리케이션 또는 웹 인터페이스 개발
  * [ ] 크롤링 중단 시 진행 상황을 저장하고 이어서 재개하는 기능 구현

-----

### 라이선스

이 프로젝트는 MIT 라이선스에 따라 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하십시오.

-----

### 문의

이 프로젝트에 대한 질문이나 제안이 있다면 언제든지 연락 주세요\!

  * **Email**: [dooseob25@gmail.com](mailto:dooseob25@gmail.com) (여기에 네 이메일 주소를 넣어줘\!)
  * **GitHub Issues**: [https://github.com/dooseob/webCrawler/issues](https://www.google.com/search?q=https://github.com/dooseob/webCrawler/issues)

-----
