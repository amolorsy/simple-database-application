public class School {
    private int schoolId;
    private String schoolName;
    private int capacity;
    private String schoolGroup;
    private float weight;
    private int applicant;

    public School(int schoolId, String schoolName, int capacity, String schoolGroup, float weight, int applicant) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.capacity = capacity;
        this.schoolGroup = schoolGroup;
        this.weight = weight;
        this.applicant = applicant;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSchoolGroup() {
        return schoolGroup;
    }

    public float getWeight() {
        return weight;
    }

    public int getApplicant() {
        return applicant;
    }
}
