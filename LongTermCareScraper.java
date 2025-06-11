package com.web.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class LongTermCareScraper {

    private static final String CHROME_DRIVER_PATH = "C:/chromedriver/chromedriver.exe"; // 네 컴퓨터 경로로 변경!

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // 개발 중에는 이 줄을 주석 처리하여 브라우저 동작을 직접 확인하세요!
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*"); // CORS 정책 우회 (최근 크롬 버전에서 필요할 수 있음)
        options.addArguments("--lang=ko"); // 브라우저 언어 설정

        WebDriver driver = new ChromeDriver(options);

        // 모든 요양기관의 추출된 데이터를 저장할 리스트
        List<FacilityEvaluationDTO> allExtractedData = new ArrayList<>();

        // 검색 결과 목록 페이지 URL (재사용을 위해 변수로 정의)
        String searchResultListUrl = "https://www.longtermcare.or.kr/npbs/r/a/201/selectXLtcoSrch.web?siDoCd=41&si_gun_gu_cd=115";

        try {
            // 1. 검색 결과 목록 페이지 접속 (경기도 수원시)
            driver.get(searchResultListUrl);
            System.out.println("1단계: 검색 결과 페이지 접속 완료 (현재 지도검색 탭).");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 충분한 대기 시간

            // ★★★ '목록검색' 탭 클릭 (가장 중요!) ★★★
            WebElement listSearchTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("C1"))); // 'C1'은 목록검색 탭의 ID
            listSearchTab.click();
            System.out.println("2단계: '목록검색' 탭 클릭 완료.");
            
            // '목록검색' 탭 내용이 로드될 때까지 기다림 (ltco_info_list 테이블이 나타날 때까지)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list"))); 
            System.out.println("목록 테이블 가시성 확인 완료.");
            Thread.sleep(2000); // 추가적인 안정성 확보를 위한 대기

            // 4. 페이지네이션 처리 시작
            int currentPage = 1;
            int totalPages = 1; 

            // ★★★ 총 페이지 수 파악 (더 안정적인 방법으로 수정) ★★★
            try {
                // .tot_txt 안에 "page" 텍스트가 포함될 때까지 기다림 (페이지 수가 완전히 로드될 때까지)
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".tot_txt"), "page"));
                WebElement totalTextElement = driver.findElement(By.cssSelector(".tot_txt span:nth-child(2)"));
                String totalPagesText = totalTextElement.getText().trim();
                
                totalPages = Integer.parseInt(totalPagesText); // "19" 같은 숫자만 있을 것이므로 바로 파싱
                System.out.println("총 페이지 수: " + totalPages);

            } catch (Exception e) {
                System.err.println("총 페이지 수 파악 오류: " + e.getMessage());
                // 오류 발생 시 기본 1페이지만 크롤링하도록 totalPages = 1 유지
            }

            while (currentPage <= totalPages) { // totalPages가 19로 제대로 파악되기를 기대!
                System.out.println("\n--- 현재 페이지: " + currentPage + " / " + totalPages + " ---");

                // ★★★ StaleElementReferenceException 방지를 위한 핵심 로직 ★★★
                // 매 페이지, 매 기관 처리 직전에 다시 요소를 찾도록 구조화
                // driver.get()이나 form.submit() 후에 항상 새로운 DOM이 로드되므로,
                // 이전에 찾았던 모든 WebElement 객체는 낡아짐.
                // 따라서, for 루프를 시작하기 전에 현재 페이지의 모든 상세 URL과 이름을 미리 추출.
                
                List<WebElement> currentTrElements = driver.findElements(By.cssSelector("table#ltco_info_list tbody tr"));
                List<String> currentDetailUrls = new ArrayList<>();
                List<String> currentFacilityNames = new ArrayList<>();
                List<List<String>> currentOtherListInfo = new ArrayList<>(); // 기타 목록 정보 저장용

                for (WebElement tr : currentTrElements) {
                    try {
                        WebElement nameLinkElement = tr.findElement(By.cssSelector("td:nth-child(3) a[name='btn_detail']")); // td 인덱스 2(세 번째)의 링크
                        
                        String facilityName = nameLinkElement.getText().trim().replaceAll("^\\d+\\.", "").trim(); // 숫자. 제거
                        String detailUrl = nameLinkElement.getAttribute("href"); // 상세 페이지 URL

                        currentDetailUrls.add(detailUrl);
                        currentFacilityNames.add(facilityName);

                        // 여기에서 나머지 목록 정보(cells.get(3)부터 cells.get(12)까지)를 미리 추출
                        List<WebElement> cells = tr.findElements(By.tagName("td"));
                        List<String> otherInfo = new ArrayList<>();
                        // 급여종류(3), 평가요약(4), 정원(5), 현원(6), 잔여(7), 대기(8), 목욕차량(9), 주소(10), 전화번호(11), 운영시간(12)
                        for (int k = 3; k <= 12; k++) { // 3부터 12까지 <td> 정보
                            try {
                                if (k == 10) { // 주소는 <a> 태그 안의 텍스트일 수 있음
                                    otherInfo.add(cells.get(k).findElement(By.tagName("a")).getText().trim());
                                } else {
                                    otherInfo.add(cells.get(k).getText().trim());
                                }
                                // ★★★ 상세 페이지 처리 후 다시 목록 페이지로 돌아가기 (핵심 해결책) 이부분이 생기고 문제 해결됨 ★★★
                            } catch (org.openqa.selenium.NoSuchElementException | IndexOutOfBoundsException | StaleElementReferenceException e) {
                                otherInfo.add(""); // 해당 셀이 없거나 오류 발생 시 빈 문자열
                            }
                        }
                        currentOtherListInfo.add(otherInfo);

                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // name='btn_detail' <a> 태그가 없는 tr은 데이터 행이 아님. 건너뛰기.
                        // System.out.println("  [디버그] 데이터가 없는 TR 스킵: " + tr.getText());
                        continue; 
                    } catch (Exception e) {
                        System.err.println("  [오류] 페이지[" + currentPage + "]에서 목록 요소 미리 추출 중 오류: " + e.getMessage());
                        e.printStackTrace();
                        // 오류 발생 시 해당 항목 건너뛰고 다음 루프 진행
                        currentDetailUrls.add(null); 
                        currentFacilityNames.add("오류 기관");
                        currentOtherListInfo.add(new ArrayList<>()); // 빈 리스트 추가
                    }
                }
                System.out.println("  현재 페이지에서 상세 URL 및 목록 정보 미리 추출 완료. 총 " + currentDetailUrls.size() + "개.");

                // 4-b. 각 요양기관에 대해 반복 처리 (미리 추출된 URL과 이름 사용)
                for (int i = 0; i < currentDetailUrls.size(); i++) {
                    String facilityName = currentFacilityNames.get(i);
                    String detailUrl = currentDetailUrls.get(i);
                    List<String> otherInfo = currentOtherListInfo.get(i);

                    // 유효성 검사 (미리 추출 단계에서 오류가 났을 경우)
                    if (detailUrl == null || detailUrl.isEmpty() || facilityName.equals("오류 기관")) {
                        System.err.println("  [건너뛰기] 유효하지 않은 기관 정보입니다: " + facilityName + " (URL: " + detailUrl + ")");
                        continue; 
                    }

                    FacilityEvaluationDTO evaluationDTO = new FacilityEvaluationDTO(); 
                    evaluationDTO.setFacilityName(facilityName);

                    // 4-b-1. 미리 추출된 목록 정보 DTO에 설정
                    if (otherInfo.size() >= 10) { // 필요한 모든 정보가 있는지 확인
                        evaluationDTO.setFacilityType(otherInfo.get(0));
                        evaluationDTO.setSummaryEvaluation(otherInfo.get(1));
                        try { evaluationDTO.setCapacity(Integer.parseInt(otherInfo.get(2))); } catch (NumberFormatException e) { evaluationDTO.setCapacity(0); }
                        try { evaluationDTO.setCurrentOccupancy(Integer.parseInt(otherInfo.get(3))); } catch (NumberFormatException e) { evaluationDTO.setCurrentOccupancy(0); }
                        try { evaluationDTO.setRemainingCapacity(Integer.parseInt(otherInfo.get(4))); } catch (NumberFormatException e) { evaluationDTO.setRemainingCapacity(0); }
                        try { evaluationDTO.setWaitingCount(Integer.parseInt(otherInfo.get(5))); } catch (NumberFormatException e) { evaluationDTO.setWaitingCount(0); }
                        evaluationDTO.setBathVehicle(otherInfo.get(6));
                        evaluationDTO.setAddress(otherInfo.get(7));
                        evaluationDTO.setPhoneNumber(otherInfo.get(8));
                        evaluationDTO.setOperationHours(otherInfo.get(9));
                    }
                    System.out.println("  -> 기관: " + facilityName + " 처리 시작 (목록 정보 DTO 설정 완료)");


                    // 4-b-2. 상세 정보 페이지로 이동
                    driver.get(detailUrl);
                    System.out.println("  상세 정보 페이지 접속 완료.");

                    // '평가정보' 탭 클릭
                    WebElement evaluationTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab_12")));
                    evaluationTab.click();
                    System.out.println("  '평가정보' 탭 클릭 완료.");

                    // 평가 정보 로드 대기
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.grade_box")));
                    Thread.sleep(500); 

                    // 4-b-3. 평가 등급 및 점수 데이터 추출 (상세 페이지에서)
                    try {
                        List<WebElement> evaluationGradeElements = driver.findElements(By.cssSelector("div.grade_box span.pink"));
                        if (!evaluationGradeElements.isEmpty()) {
                            evaluationDTO.setEvaluationGrade(evaluationGradeElements.get(0).getText().trim()); 
                        }
                    } catch (Exception e) { System.err.println("  [오류] 상세 평가 등급 추출 오류: " + e.getMessage()); }

                    try {
                        WebElement scoreTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.tbl_col.tbl_point table")));
                        List<WebElement> scoreRows = scoreTable.findElements(By.tagName("tr"));
                        
                        if (scoreRows.size() > 3) { 
                            WebElement scoreDataRow = scoreRows.get(3); 
                            List<WebElement> scores = scoreDataRow.findElements(By.tagName("td"));
                            
                            if (scores.size() >= 6) { 
                                try { evaluationDTO.setTotalScore(Integer.parseInt(scores.get(0).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setTotalScore(0); }
                                try { evaluationDTO.setManagementScore(Integer.parseInt(scores.get(1).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setManagementScore(0); }
                                try { evaluationDTO.setEnvironmentScore(Integer.parseInt(scores.get(2).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setEnvironmentScore(0); }
                                try { evaluationDTO.setRightsScore(Integer.parseInt(scores.get(3).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setRightsScore(0); }
                                try { evaluationDTO.setProcessScore(Integer.parseInt(scores.get(4).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setProcessScore(0); }
                                try { evaluationDTO.setResultScore(Integer.parseInt(scores.get(5).getText().trim())); } catch (NumberFormatException e) { evaluationDTO.setResultScore(0); }
                            }
                        }
                    } catch (Exception e) { System.err.println("  [오류] 평가 항목별 점수 추출 오류: " + e.getMessage()); }

                    // ★★★ allExtractedData 리스트를 DB에 일괄 저장하는 로직
                    allExtractedData.add(evaluationDTO); 
                    System.out.println("  >>> 추출된 데이터: " + evaluationDTO.toString());

                    // ★★★ 상세 페이지 처리 후 다시 목록 페이지로 돌아가기 (핵심 해결책) ★★★
                    // 매 기관 처리 후, driver.get()으로 원래 목록 페이지로 돌아와야 다음 기관을 찾을 수 있음.
                    driver.get(searchResultListUrl); 
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list")));
                    Thread.sleep(1000); 
                    // 목록검색 탭이 다시 지도검색으로 돌아갈 수 있으므로 다시 클릭 (필요시)
                    // 지금은 driver.get()으로 돌아가면 자동으로 C1 탭이 on 상태로 유지될 것임
                    
                } // 현재 페이지 내 요양기관 루프 끝

                // 4-d. 다음 페이지로 이동
                currentPage++;
                if (currentPage <= totalPages) {
                    System.out.println("\n--- 다음 페이지로 이동: " + currentPage + " ---");
                    WebElement currentPageNoInput = driver.findElement(By.id("cu_pag_no"));
                    ((org.openqa.selenium.JavascriptExecutor)driver).executeScript("arguments[0].value = arguments[1];", currentPageNoInput, String.valueOf(currentPage));

                    WebElement form = driver.findElement(By.id("ltco_info")); 
                    form.submit(); 
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list")));
                    Thread.sleep(2000); 
                }

            } // 전체 페이지 루프 끝

            System.out.println("\n--- 모든 요양기관 목록 및 평가 정보 추출 완료 ---");
            System.out.println("총 추출된 기관 수: " + allExtractedData.size());

        } catch (Exception e) {
            System.err.println("크롤링 전체 과정에서 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit(); 
            }
        }
    }
}
