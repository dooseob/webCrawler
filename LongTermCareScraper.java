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

    private static final String CHROME_DRIVER_PATH = "C:/chromedriver/chromedriver.exe"; // �� ��ǻ�� ��η� ����!

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // ���� �߿��� �� ���� �ּ� ó���Ͽ� ������ ������ ���� Ȯ���ϼ���!
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*"); // CORS ��å ��ȸ (�ֱ� ũ�� �������� �ʿ��� �� ����)
        options.addArguments("--lang=ko"); // ������ ��� ����

        WebDriver driver = new ChromeDriver(options);

        // ��� ������� ����� �����͸� ������ ����Ʈ
        List<FacilityEvaluationDTO> allExtractedData = new ArrayList<>();

        // �˻� ��� ��� ������ URL (������ ���� ������ ����)
        String searchResultListUrl = "https://www.longtermcare.or.kr/npbs/r/a/201/selectXLtcoSrch.web?siDoCd=41&si_gun_gu_cd=115";

        try {
            // 1. �˻� ��� ��� ������ ���� (��⵵ ������)
            driver.get(searchResultListUrl);
            System.out.println("1�ܰ�: �˻� ��� ������ ���� �Ϸ� (���� �����˻� ��).");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // ����� ��� �ð�

            // �ڡڡ� '��ϰ˻�' �� Ŭ�� (���� �߿�!) �ڡڡ�
            WebElement listSearchTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("C1"))); // 'C1'�� ��ϰ˻� ���� ID
            listSearchTab.click();
            System.out.println("2�ܰ�: '��ϰ˻�' �� Ŭ�� �Ϸ�.");
            
            // '��ϰ˻�' �� ������ �ε�� ������ ��ٸ� (ltco_info_list ���̺��� ��Ÿ�� ������)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list"))); 
            System.out.println("��� ���̺� ���ü� Ȯ�� �Ϸ�.");
            Thread.sleep(2000); // �߰����� ������ Ȯ���� ���� ���

            // 4. ���������̼� ó�� ����
            int currentPage = 1;
            int totalPages = 1; 

            // �ڡڡ� �� ������ �� �ľ� (�� �������� ������� ����) �ڡڡ�
            try {
                // .tot_txt �ȿ� "page" �ؽ�Ʈ�� ���Ե� ������ ��ٸ� (������ ���� ������ �ε�� ������)
                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".tot_txt"), "page"));
                WebElement totalTextElement = driver.findElement(By.cssSelector(".tot_txt span:nth-child(2)"));
                String totalPagesText = totalTextElement.getText().trim();
                
                totalPages = Integer.parseInt(totalPagesText); // "19" ���� ���ڸ� ���� ���̹Ƿ� �ٷ� �Ľ�
                System.out.println("�� ������ ��: " + totalPages);

            } catch (Exception e) {
                System.err.println("�� ������ �� �ľ� ����: " + e.getMessage());
                // ���� �߻� �� �⺻ 1�������� ũ�Ѹ��ϵ��� totalPages = 1 ����
            }

            while (currentPage <= totalPages) { // totalPages�� 19�� ����� �ľǵǱ⸦ ���!
                System.out.println("\n--- ���� ������: " + currentPage + " / " + totalPages + " ---");

                // �ڡڡ� StaleElementReferenceException ������ ���� �ٽ� ���� �ڡڡ�
                // �� ������, �� ��� ó�� ������ �ٽ� ��Ҹ� ã���� ����ȭ
                // driver.get()�̳� form.submit() �Ŀ� �׻� ���ο� DOM�� �ε�ǹǷ�,
                // ������ ã�Ҵ� ��� WebElement ��ü�� ������.
                // ����, for ������ �����ϱ� ���� ���� �������� ��� �� URL�� �̸��� �̸� ����.
                
                List<WebElement> currentTrElements = driver.findElements(By.cssSelector("table#ltco_info_list tbody tr"));
                List<String> currentDetailUrls = new ArrayList<>();
                List<String> currentFacilityNames = new ArrayList<>();
                List<List<String>> currentOtherListInfo = new ArrayList<>(); // ��Ÿ ��� ���� �����

                for (WebElement tr : currentTrElements) {
                    try {
                        WebElement nameLinkElement = tr.findElement(By.cssSelector("td:nth-child(3) a[name='btn_detail']")); // td �ε��� 2(�� ��°)�� ��ũ
                        
                        String facilityName = nameLinkElement.getText().trim().replaceAll("^\\d+\\.", "").trim(); // ����. ����
                        String detailUrl = nameLinkElement.getAttribute("href"); // �� ������ URL

                        currentDetailUrls.add(detailUrl);
                        currentFacilityNames.add(facilityName);

                        // ���⿡�� ������ ��� ����(cells.get(3)���� cells.get(12)����)�� �̸� ����
                        List<WebElement> cells = tr.findElements(By.tagName("td"));
                        List<String> otherInfo = new ArrayList<>();
                        // �޿�����(3), �򰡿��(4), ����(5), ����(6), �ܿ�(7), ���(8), �������(9), �ּ�(10), ��ȭ��ȣ(11), ��ð�(12)
                        for (int k = 3; k <= 12; k++) { // 3���� 12���� <td> ����
                            try {
                                if (k == 10) { // �ּҴ� <a> �±� ���� �ؽ�Ʈ�� �� ����
                                    otherInfo.add(cells.get(k).findElement(By.tagName("a")).getText().trim());
                                } else {
                                    otherInfo.add(cells.get(k).getText().trim());
                                }
                            } catch (org.openqa.selenium.NoSuchElementException | IndexOutOfBoundsException | StaleElementReferenceException e) {
                                otherInfo.add(""); // �ش� ���� ���ų� ���� �߻� �� �� ���ڿ�
                            }
                        }
                        currentOtherListInfo.add(otherInfo);

                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // name='btn_detail' <a> �±װ� ���� tr�� ������ ���� �ƴ�. �ǳʶٱ�.
                        // System.out.println("  [�����] �����Ͱ� ���� TR ��ŵ: " + tr.getText());
                        continue; 
                    } catch (Exception e) {
                        System.err.println("  [����] ������[" + currentPage + "]���� ��� ��� �̸� ���� �� ����: " + e.getMessage());
                        e.printStackTrace();
                        // ���� �߻� �� �ش� �׸� �ǳʶٰ� ���� ���� ����
                        currentDetailUrls.add(null); 
                        currentFacilityNames.add("���� ���");
                        currentOtherListInfo.add(new ArrayList<>()); // �� ����Ʈ �߰�
                    }
                }
                System.out.println("  ���� ���������� �� URL �� ��� ���� �̸� ���� �Ϸ�. �� " + currentDetailUrls.size() + "��.");

                // 4-b. �� ������� ���� �ݺ� ó�� (�̸� ����� URL�� �̸� ���)
                for (int i = 0; i < currentDetailUrls.size(); i++) {
                    String facilityName = currentFacilityNames.get(i);
                    String detailUrl = currentDetailUrls.get(i);
                    List<String> otherInfo = currentOtherListInfo.get(i);

                    // ��ȿ�� �˻� (�̸� ���� �ܰ迡�� ������ ���� ���)
                    if (detailUrl == null || detailUrl.isEmpty() || facilityName.equals("���� ���")) {
                        System.err.println("  [�ǳʶٱ�] ��ȿ���� ���� ��� �����Դϴ�: " + facilityName + " (URL: " + detailUrl + ")");
                        continue; 
                    }

                    FacilityEvaluationDTO evaluationDTO = new FacilityEvaluationDTO(); 
                    evaluationDTO.setFacilityName(facilityName);

                    // 4-b-1. �̸� ����� ��� ���� DTO�� ����
                    if (otherInfo.size() >= 10) { // �ʿ��� ��� ������ �ִ��� Ȯ��
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
                    System.out.println("  -> ���: " + facilityName + " ó�� ���� (��� ���� DTO ���� �Ϸ�)");


                    // 4-b-2. �� ���� �������� �̵�
                    driver.get(detailUrl);
                    System.out.println("  �� ���� ������ ���� �Ϸ�.");

                    // '������' �� Ŭ��
                    WebElement evaluationTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab_12")));
                    evaluationTab.click();
                    System.out.println("  '������' �� Ŭ�� �Ϸ�.");

                    // �� ���� �ε� ���
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.grade_box")));
                    Thread.sleep(500); 

                    // 4-b-3. �� ��� �� ���� ������ ���� (�� ����������)
                    try {
                        List<WebElement> evaluationGradeElements = driver.findElements(By.cssSelector("div.grade_box span.pink"));
                        if (!evaluationGradeElements.isEmpty()) {
                            evaluationDTO.setEvaluationGrade(evaluationGradeElements.get(0).getText().trim()); 
                        }
                    } catch (Exception e) { System.err.println("  [����] �� �� ��� ���� ����: " + e.getMessage()); }

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
                    } catch (Exception e) { System.err.println("  [����] �� �׸� ���� ���� ����: " + e.getMessage()); }

                    allExtractedData.add(evaluationDTO); 
                    System.out.println("  >>> ����� ������: " + evaluationDTO.toString());

                    // �ڡڡ� �� ������ ó�� �� �ٽ� ��� �������� ���ư��� (�ٽ� �ذ�å) �ڡڡ�
                    // �� ��� ó�� ��, driver.get()���� ���� ��� �������� ���ƿ;� ���� ����� ã�� �� ����.
                    driver.get(searchResultListUrl); 
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list")));
                    Thread.sleep(1000); 
                    // ��ϰ˻� ���� �ٽ� �����˻����� ���ư� �� �����Ƿ� �ٽ� Ŭ�� (�ʿ��)
                    // ������ driver.get()���� ���ư��� �ڵ����� C1 ���� on ���·� ������ ����
                    
                } // ���� ������ �� ����� ���� ��

                // 4-d. ���� �������� �̵�
                currentPage++;
                if (currentPage <= totalPages) {
                    System.out.println("\n--- ���� �������� �̵�: " + currentPage + " ---");
                    WebElement currentPageNoInput = driver.findElement(By.id("cu_pag_no"));
                    ((org.openqa.selenium.JavascriptExecutor)driver).executeScript("arguments[0].value = arguments[1];", currentPageNoInput, String.valueOf(currentPage));

                    WebElement form = driver.findElement(By.id("ltco_info")); 
                    form.submit(); 
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ltco_info_list")));
                    Thread.sleep(2000); 
                }

            } // ��ü ������ ���� ��

            System.out.println("\n--- ��� ����� ��� �� �� ���� ���� �Ϸ� ---");
            System.out.println("�� ����� ��� ��: " + allExtractedData.size());

        } catch (Exception e) {
            System.err.println("ũ�Ѹ� ��ü �������� ���� �߻�: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit(); 
            }
        }
    }
}