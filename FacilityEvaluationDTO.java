package com.web.crawler;

public class FacilityEvaluationDTO {
	
	// ��� ����
    private String facilityName;        // ����� �̸� (cells.get(2)�� <a> �ؽ�Ʈ)
    private String facilityType;        // �޿����� (cells.get(3))
    private String summaryEvaluation;   // �򰡰�� ��� (cells.get(4))
    private int capacity;               // ���� (cells.get(5))
    private int currentOccupancy;       // ���� (cells.get(6))
    private int remainingCapacity;      // �ܿ� (cells.get(7))
    private int waitingCount;           // ��� (cells.get(8))
    private String bathVehicle;         // ������� (cells.get(9))
    private String address;             // �ּ� (cells.get(10)�� <a> �ؽ�Ʈ)
    private String phoneNumber;         // ��ȭ��ȣ (cells.get(11))
    private String operationHours;      // ��ð� (cells.get(12))

    // �� �� ���� (evaluationGrade�� summaryEvaluation�� �ߺ��� �� ������, �� ���� ��� ǥ�ÿ����� ����)
    private String evaluationGrade;     // �� �� ��� (div.grade_box span.pink)
    private int totalScore;             // ���� (ǥ����)
    private int managementScore;        // ���� (ǥ����)
    private int environmentScore;       // ȯ��׾��� (ǥ����)
    private int rightsScore;            // �����ڱǸ����� (ǥ����)
    private int processScore;           // �޿��������� (ǥ����)
    private int resultScore;            // �޿�������� (ǥ����)

    // Getters and Setters (���� - �ʿ�� �߰�)
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



