public class Student {
    private int studentId;
    private String studentName;
    private int csatScore;
    private int schoolRecordsScore;

    public Student(int studentId, String studentName, int csatScore, int schoolRecordsScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.csatScore = csatScore;
        this.schoolRecordsScore = schoolRecordsScore;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCsatScore() {
        return csatScore;
    }

    public int getSchoolRecordsScore() {
        return schoolRecordsScore;
    }
}
