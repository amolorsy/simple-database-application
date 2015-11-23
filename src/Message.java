public class Message {
    public static final int INTRODUCTION = -1;
    public static final int INSERT_SCHOOL_SUCCESS = 0;
    public static final int DELETE_SCHOOL_SUCCESS = 1;
    public static final int INSERT_STUDENT_SUCCESS = 2;
    public static final int DELETE_STUDENT_SUCCESS = 3;
    public static final int CAPACITY_SCOPE_ERROR = 4;
    public static final int SCHOOL_GROUP_ERROR = 5;
    public static final int WEIGHT_SCOPE_ERROR = 6;
    public static final int CSAT_SCORE_SCOPE_ERROR = 7;
    public static final int SCHOOL_RECORDS_SCOPE_ERROR = 8;
    public static final int SUBMIT_APPLICATION_SUCCESS = 9;
    public static final int SUBMIT_APPLICATION_FAILURE = 10;
    public static final int SELECT_MENU_ERROR = 11;
    public static final int NON_EXIST_SCHOOL_ID = 12;
    public static final int NON_EXIST_STUDENT_ID = 13;

    public static void print(int messageType, String word) {
        switch (messageType) {
        case INTRODUCTION:
            System.out.print("============================================================\n"
                + "1. print all universities\n" + "2. print all students\n" + "3. insert a new university\n"
                + "4. remove a university\n" + "5. insert a new student\n" + "6. remove a student\n"
                + "7. make an application\n" + "8. print all students who applied for a university\n"
                + "9. print all universities a student applied for\n"
                + "10. print expected successful applicants of a university\n"
                + "11. print universities expected to accept a student\n" + "12. exit\n"
                + "============================================================\n" + "Select you action: ");
            break;
        case INSERT_SCHOOL_SUCCESS:
            System.out.println("A university is successfully inserted.");
            break;
        case DELETE_SCHOOL_SUCCESS:
            System.out.println("A university is successfully deleted.");
            break;
        case INSERT_STUDENT_SUCCESS:
            System.out.println("A student is successfully inserted.");
            break;
        case DELETE_STUDENT_SUCCESS:
            System.out.println("A student is successfully deleted.");
            break;
        case CAPACITY_SCOPE_ERROR:
            System.out.println("Capacity should be over 0.");
            break;
        case SCHOOL_GROUP_ERROR:
            System.out.println("Group should be 'A', 'B', or 'C'.");
            break;
        case WEIGHT_SCOPE_ERROR:
            System.out.println("Weight of high school records cannot be negative.");
            break;
        case CSAT_SCORE_SCOPE_ERROR:
            System.out.println("CSAT score should be between 0 and 400.");
            break;
        case SCHOOL_RECORDS_SCOPE_ERROR:
            System.out.println("High school records score should be between 0 and 100.");
            break;
        case SUBMIT_APPLICATION_SUCCESS:
            System.out.println("Successfully made an application.");
            break;
        case SUBMIT_APPLICATION_FAILURE:
            System.out.println("A student can apply up to one university per group.");
            break;
        case SELECT_MENU_ERROR:
            System.out.println("Invalid action.");
            break;
        case NON_EXIST_SCHOOL_ID:
            System.out.println("University " + word + " doesn't exist.");
            break;
        case NON_EXIST_STUDENT_ID:
            System.out.println("Student " + word + " doesn't exist.");
            break;
        }
    }
}
