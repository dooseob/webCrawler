package com.web.crawler;

public class FacilityEvaluationDTO {
	
	// 목록 정보
    private String facilityName;        // 요양기관 이름 (cells.get(2)의 <a> 텍스트)
    private String facilityType;        // 급여종류 (cells.get(3))
    private String summaryEvaluation;   // 평가결과 요약 (cells.get(4))
    private int capacity;               // 정원 (cells.get(5))
    private int currentOccupancy;       // 현원 (cells.get(6))
    private int remainingCapacity;      // 잔여 (cells.get(7))
    private int waitingCount;           // 대기 (cells.get(8))
    private String bathVehicle;         // 목욕차량 (cells.get(9))
    private String address;             // 주소 (cells.get(10)의 <a> 텍스트)
    private String phoneNumber;         // 전화번호 (cells.get(11))
    private String operationHours;      // 운영시간 (cells.get(12))

    // 상세 평가 정보 (evaluationGrade는 summaryEvaluation과 중복될 수 있으나, 더 상세한 등급 표시용으로 남김)
    private String evaluationGrade;     // 상세 평가 등급 (div.grade_box span.pink)
    private int totalScore;             // 총점 (표에서)
    private int managementScore;        // 기관운영 (표에서)
    private int environmentScore;       // 환경및안전 (표에서)
    private int rightsScore;            // 수급자권리보장 (표에서)
    private int processScore;           // 급여제공과정 (표에서)
    private int resultScore;            // 급여제공결과 (표에서)

    // Getters and Setters (생략 - 필요시 추가)
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
    public String getFacilityType() { return facilityType; }
    public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
    public String getSummaryEvaluation() { return summaryEvaluation; }
    public void setSummaryEvaluation(String summaryEvaluation) { this.summaryEvaluation = summaryEvaluation; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }
    public int getRemainingCapacity() { return remainingCapacity; }
    public void setRemainingCapacity(int remainingCapacity) { this.remainingCapacity = remainingCapacity; }
    public int getWaitingCount() { return waitingCount; }
    public void setWaitingCount(int waitingCount) { this.waitingCount = waitingCount; }
    public String getBathVehicle() { return bathVehicle; }
    public void setBathVehicle(String bathVehicle) { this.bathVehicle = bathVehicle; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getOperationHours() { return operationHours; }
    public void setOperationHours(String operationHours) { this.operationHours = operationHours; }
    public String getEvaluationGrade() { return evaluationGrade; }
    public void setEvaluationGrade(String evaluationGrade) { this.evaluationGrade = evaluationGrade; }
    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public int getManagementScore() { return managementScore; }
    public void setManagementScore(int managementScore) { this.managementScore = managementScore; }
    public int getEnvironmentScore() { return environmentScore; }
    public void setEnvironmentScore(int environmentScore) { this.environmentScore = environmentScore; }
    public int getRightsScore() { return rightsScore; }
    public void setRightsScore(int rightsScore) { this.rightsScore = rightsScore; }
    public int getProcessScore() { return processScore; }
    public void setProcessScore(int processScore) { this.processScore = processScore; }
    public int getResultScore() { return resultScore; }
    public void setResultScore(int resultScore) { this.resultScore = resultScore; }

    @Override
    public String toString() {
        return "FacilityEvaluationDTO{" +
               "facilityName='" + facilityName + '\'' +
               ", facilityType='" + facilityType + '\'' +
               ", summaryEvaluation='" + summaryEvaluation + '\'' +
               ", capacity=" + capacity +
               ", currentOccupancy=" + currentOccupancy +
               ", remainingCapacity=" + remainingCapacity +
               ", waitingCount=" + waitingCount +
               ", bathVehicle='" + bathVehicle + '\'' +
               ", address='" + address + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", operationHours='" + operationHours + '\'' +
               ", evaluationGrade='" + evaluationGrade + '\'' +
               ", totalScore=" + totalScore +
               ", managementScore=" + managementScore +
               ", environmentScore=" + environmentScore +
               ", rightsScore=" + rightsScore +
               ", processScore=" + processScore +
               ", resultScore=" + resultScore +
               '}';
    }
}



