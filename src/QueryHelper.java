import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class QueryHelper {
    public static void command(String type) throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String schoolName, schoolGroup, studentName;
        int schoolId, studentId, capacity, csatScore, schoolRecordsScore;
        Float weight;
        boolean isSuccess;

        switch (type) {
        case "1":
            print(select("SELECT * FROM school ORDER BY school_id;"));
            break;
        case "2":
            print(select("SELECT * FROM student ORDER BY student_id;"));
            break;
        case "3":
            System.out.print("University name: ");
            schoolName = br.readLine();

            System.out.print("University capacity: ");
            capacity = Integer.parseInt(br.readLine());
            if (capacity < 1) {
                Message.print(Message.CAPACITY_SCOPE_ERROR, null);
                break;
            }

            System.out.print("University group: ");
            schoolGroup = br.readLine();
            if (!schoolGroup.equals("A") && !schoolGroup.equals("B") && !schoolGroup.equals("C")) {
                Message.print(Message.SCHOOL_GROUP_ERROR, null);
                break;
            }

            System.out.print("Weight of high school records: ");
            weight = Float.parseFloat(br.readLine());
            if (weight < 0) {
                Message.print(Message.WEIGHT_SCOPE_ERROR, null);
                break;
            }

            isSuccess = insert(schoolName, capacity, schoolGroup, weight);
            if (isSuccess)
                Message.print(Message.INSERT_SCHOOL_SUCCESS, null);
            break;
        case "4":
            System.out.print("University ID: ");
            schoolId = Integer.parseInt(br.readLine());

            isSuccess = delete("DELETE FROM school WHERE school_id = " + schoolId + ";");

            if (isSuccess)
                Message.print(Message.DELETE_SCHOOL_SUCCESS, null);
            else
                Message.print(Message.NON_EXIST_SCHOOL_ID, String.valueOf(schoolId));
            break;
        case "5":
            System.out.print("Student name: ");
            studentName = br.readLine();

            System.out.print("CSAT score: ");
            csatScore = Integer.parseInt(br.readLine());
            if (csatScore < 0 || csatScore > 400) {
                Message.print(Message.CSAT_SCORE_SCOPE_ERROR, null);
                break;
            }

            System.out.print("High school record score: ");
            schoolRecordsScore = Integer.parseInt(br.readLine());
            if (schoolRecordsScore < 0 || schoolRecordsScore > 100) {
                Message.print(Message.SCHOOL_RECORDS_SCOPE_ERROR, null);
                break;
            }

            isSuccess = insert(studentName, csatScore, schoolRecordsScore);
            if (isSuccess)
                Message.print(Message.INSERT_STUDENT_SUCCESS, null);
            break;
        case "6":
            System.out.print("Student ID: ");
            studentId = Integer.parseInt(br.readLine());

            // Update applicant as (applicant - 1) because one student is deleted.
            update("UPDATE school, application SET school.applicant = school.applicant - 1"
                    + " WHERE school.school_id = application.school_id" + " AND application.student_id = " + studentId
                    + ";");
            isSuccess = delete("DELETE FROM student WHERE student_id = " + studentId + ";");

            if (isSuccess)
                Message.print(Message.DELETE_STUDENT_SUCCESS, null);
            else
                Message.print(Message.NON_EXIST_STUDENT_ID, String.valueOf(studentId));
            break;
        case "7":
            System.out.print("Student ID: ");
            studentId = Integer.parseInt(br.readLine());
            if (!isValid("student", "student_id", studentId)) {
                /* student_id doesn't exist. */
                Message.print(Message.NON_EXIST_STUDENT_ID, String.valueOf(studentId));
                break;
            }

            System.out.print("University ID: ");
            schoolId = Integer.parseInt(br.readLine());
            if (!isValid("school", "school_id", schoolId)) {
                /* school_id doesn't exist. */
                Message.print(Message.NON_EXIST_SCHOOL_ID, String.valueOf(schoolId));
                break;
            }

            if (hasApplication(studentId, getSchoolGroup(schoolId))) {
                /* A student can apply up to one university per group. */
                Message.print(Message.SUBMIT_APPLICATION_FAILURE, null);
            } else {
                isSuccess = insert(studentId, schoolId);

                if (isSuccess) {
                    Message.print(Message.SUBMIT_APPLICATION_SUCCESS, null);
                    update("UPDATE school SET applicant = applicant + 1 WHERE school_id = " + schoolId + ";");
                }
            }
            break;
        case "8":
            System.out.print("University ID: ");
            schoolId = Integer.parseInt(br.readLine());
            if (!isValid("school", "school_id", schoolId)) {
                Message.print(Message.NON_EXIST_SCHOOL_ID, String.valueOf(schoolId));
                break;
            }

            print(select(
                    "SELECT student.student_id, student.student_name, student.csat_score, student.school_records_score"
                            + " FROM student, application" + " WHERE application.school_id = " + schoolId
                            + " AND student.student_id = application.student_id" + " ORDER BY student.student_id;"));
            break;
        case "9":
            System.out.print("Student ID: ");
            studentId = Integer.parseInt(br.readLine());
            if (!isValid("student", "student_id", studentId)) {
                Message.print(Message.NON_EXIST_STUDENT_ID, String.valueOf(studentId));
                break;
            }

            print(select(
                    "SELECT school.school_id, school.school_name, school.capacity, school.school_group, school.weight, school.applicant"
                            + " FROM school, application" + " WHERE application.student_id = " + studentId
                            + " AND school.school_id = application.school_id" + " ORDER BY school.school_id;"));
            break;
        case "10":
            System.out.println("University ID: ");
            schoolId = Integer.parseInt(br.readLine());
            if (!isValid("school", "school_id", schoolId)) {
                Message.print(Message.NON_EXIST_SCHOOL_ID, String.valueOf(schoolId));
                break;
            }

            showAcceptedStudents(schoolId);
            break;
        case "11":
            System.out.print("Student ID: ");
            studentId = Integer.parseInt(br.readLine());
            if (!isValid("student", "student_id", studentId)) {
                Message.print(Message.NON_EXIST_STUDENT_ID, String.valueOf(studentId));
                break;
            }

            showAcceptedSchools(studentId);
            break;
        case "12":
            System.exit(0);
            break;
        default:
            Message.print(Message.SELECT_MENU_ERROR, null); /* Invalid action */
            break;
        }
    }

    /* Return a specific record according to the result of SQL */
    private static ResultSet getRecord(String sql) throws SQLException {
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet;
    }

    /* Return whether parameter id is included in column values or not */
    private static boolean isValid(String tableName, String columnName, int id) throws SQLException {
        String sql = "SELECT count(*) AS total FROM " + tableName + " WHERE " + columnName + " = " + id + ";";
        return getRecord(sql).getInt("total") == 1 ? true : false;
    }

    /*
     * Return whether the student whose student ID is parameter studentId applied to
     * a school in parameter schoolGroup or not
     */
    private static boolean hasApplication(int studentId, String schoolGroup) throws SQLException {
        if (schoolGroup == null)
            return false;

        String sql = "SELECT count(*) AS total" + " FROM school NATURAL JOIN application" + " WHERE student_id = "
                + studentId + " AND school_group = " + "'" + schoolGroup + "'" + ";";
        return getRecord(sql).getInt("total") == 0 ? false : true;
    }

    private static String getSchoolGroup(int schoolId) throws SQLException {
        String sql = "SELECT school_group FROM school WHERE school_id = " + schoolId + ";";
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next() ? resultSet.getString("school_group") : null;
    }

    private static int getRecordId(String sql, String word) throws SQLException {
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next() ? resultSet.getInt(word) + 1 : 1;
    }

    public static ResultSet select(String sql) throws SQLException {
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public static void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 1; i <= metaData.getColumnCount(); i++)
            System.out.print(metaData.getColumnName(i) + "\t");
        System.out.println("\n--------------------------------------------------------------------------------");

        while (resultSet.next()) {
            int columnCount = metaData.getColumnCount();

            // Column count starts from 1.
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);

                switch (columnType) {
                case Types.VARCHAR:
                case Types.CHAR:
                    System.out.print(resultSet.getString(columnName).toString() + "\t");
                    break;
                case Types.INTEGER:
                    System.out.print(resultSet.getInt(columnName) + "\t");
                    break;
                case Types.REAL:
                    System.out.print((Math.round(resultSet.getFloat(columnName) * 1000.0) / 1000.0) + "\t");
                    break;
                }
            }
            System.out.println("\n--------------------------------------------------------------------------------");
        }
    }

    private static void showAcceptedStudents(int schoolId) throws SQLException {
        ResultSet record = getRecord("SELECT * FROM school WHERE school_id = " + schoolId);
        int capacity = record.getInt("capacity");
        int limit = (int) Math.ceil(record.getInt("capacity") * 1.1);
        String ordered = "SELECT student_id, student_name, csat_score, school_records_score"
                + " FROM application NATURAL JOIN student NATURAL JOIN school" + " WHERE school_id = " + schoolId
                + " ORDER BY (csat_score + school_records_score * weight) DESC, school_records_score DESC, student_id"
                + " LIMIT " + (limit + 1) + ";";
        ResultSet resultSet = select(ordered);
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<Student> resultList = new ArrayList<Student>();

        while (resultSet.next()) {
            resultList.add(
                    new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));
        }

        if (resultList.size() - 1 <= capacity) {
            if (resultList.size() - 1 == capacity)
                resultList.remove(resultList.size() - 1);

            System.out.println("--------------------------------------------------------------------------------");
            for (int i = 1; i <= metaData.getColumnCount(); i++)
                System.out.print(metaData.getColumnName(i) + "\t");
            System.out.println("\n--------------------------------------------------------------------------------");

            for (Student student : resultList) {
                System.out.print(student.getStudentId() + "\t");
                System.out.print(student.getStudentName() + "\t");
                System.out.print(student.getCsatScore() + " \t");
                System.out.print(student.getSchoolRecordsScore());
                System.out
                        .println("\n--------------------------------------------------------------------------------");
            }
        } else {
            int count = capacity - 1, lastIndex = count;
            while (count <= resultList.size() - 1) {
                Student studentA = resultList.get(count - 1);
                Student studentB = resultList.get(count);

                if (studentA.getCsatScore() == studentB.getCsatScore()
                        && studentA.getSchoolRecordsScore() == studentB.getSchoolRecordsScore()) {
                    lastIndex++;
                }
                count++;
            }

            if (lastIndex == resultList.size() - 1) {
                while (--lastIndex >= 0) {
                    Student studentA = resultList.get(lastIndex);
                    Student studentB = resultList.get(lastIndex + 1);

                    if (!(studentA.getCsatScore() == studentB.getCsatScore()
                            && studentA.getSchoolRecordsScore() == studentB.getSchoolRecordsScore())) {
                        break;
                    }
                }
            }

            int index = resultList.size() - 1;
            while (index > lastIndex) {
                resultList.remove(index--);
            }

            System.out.println("--------------------------------------------------------------------------------");
            for (int i = 1; i <= metaData.getColumnCount(); i++)
                System.out.print(metaData.getColumnName(i) + "\t");
            System.out.println("\n--------------------------------------------------------------------------------");

            for (Student student : resultList) {
                System.out.print(student.getStudentId() + "\t");
                System.out.print(student.getStudentName() + "\t");
                System.out.print(student.getCsatScore() + " \t");
                System.out.print(student.getSchoolRecordsScore());
                System.out
                        .println("\n--------------------------------------------------------------------------------");
            }
        }
    }

    private static void showAcceptedSchools(int studentId) throws SQLException {
        ResultSet record = getRecord("SELECT * FROM school");
        List<School> schoolList = new ArrayList<School>();

        do {
            int schoolId = record.getInt("school_id");
            String schoolName = record.getString("school_name");
            int capacity = record.getInt("capacity");
            String schoolGroup = record.getString("school_group");
            float weight = record.getFloat("weight");
            int applicant = record.getInt("applicant");

            int limit = (int) Math.ceil(record.getInt("capacity") * 1.1);
            String ordered = "SELECT student_id, student_name, csat_score, school_records_score"
                    + " FROM application NATURAL JOIN student NATURAL JOIN school" + " WHERE school_id = " + schoolId
                    + " ORDER BY (csat_score + school_records_score * weight) DESC, school_records_score DESC, student_id"
                    + " LIMIT " + (limit + 1) + ";";
            ResultSet resultSet = select(ordered);
            List<Student> resultList = new ArrayList<Student>();

            while (resultSet.next()) {
                resultList.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
                        resultSet.getInt(4)));
            }

            if (resultList.size() - 1 <= capacity) {
                if (resultList.size() - 1 == capacity)
                    resultList.remove(resultList.size() - 1);
            } else {
                int count = capacity - 1, lastIndex = count;
                while (count <= resultList.size() - 1) {
                    Student studentA = resultList.get(count - 1);
                    Student studentB = resultList.get(count);

                    if (studentA.getCsatScore() == studentB.getCsatScore()
                            && studentA.getSchoolRecordsScore() == studentB.getSchoolRecordsScore()) {
                        lastIndex++;
                    }
                    count++;
                }

                if (lastIndex == resultList.size() - 1) {
                    while (--lastIndex >= 0) {
                        Student studentA = resultList.get(lastIndex);
                        Student studentB = resultList.get(lastIndex + 1);

                        if (!(studentA.getCsatScore() == studentB.getCsatScore()
                                && studentA.getSchoolRecordsScore() == studentB.getSchoolRecordsScore())) {
                            break;
                        }
                    }
                }

                int index = resultList.size() - 1;
                while (index > lastIndex) {
                    resultList.remove(index--);
                }
            }

            for (Student student : resultList) {
                if (student.getStudentId() == studentId) {
                    schoolList.add(new School(schoolId, schoolName, capacity, schoolGroup, weight, applicant));
                    break;
                }
            }
        } while (record.next());

        ResultSetMetaData metaData = record.getMetaData();
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 1; i <= metaData.getColumnCount(); i++)
            System.out.print(metaData.getColumnName(i) + "\t");
        System.out.println("\n--------------------------------------------------------------------------------");

        for (School school : schoolList) {
            System.out.print(school.getSchoolId() + "\t");
            System.out.print(school.getSchoolName() + "\t");
            System.out.print(school.getCapacity() + " \t");
            System.out.print(school.getSchoolGroup() + " \t");
            System.out.print(school.getWeight() + " \t");
            System.out.print(school.getApplicant());
            System.out.println("\n--------------------------------------------------------------------------------");
        }
    }

    /* Function insert for school table */
    private static boolean insert(String schoolName, int capacity, String schoolGroup, float weight)
            throws SQLException {
        int schoolId = getRecordId(
                "SELECT school_id FROM school WHERE school_id in (SELECT max(school_id) FROM school);", "school_id");
        String sql = "INSERT INTO school(school_id, school_name, capacity, school_group, weight) "
                + "VALUES(?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, schoolId);
        preparedStatement.setString(2, schoolName.length() < 128 ? schoolName : schoolName.substring(0, 128));
        preparedStatement.setInt(3, capacity);
        preparedStatement.setString(4, schoolGroup);
        preparedStatement.setFloat(5, weight);

        return preparedStatement.executeUpdate() == 1 ? true : false;
    }

    /* Function insert for student table */
    private static boolean insert(String studentName, int csatScore, int schoolRecordsScore) throws SQLException {
        int studentId = getRecordId(
                "SELECT student_id FROM student WHERE student_id in (SELECT max(student_id) FROM student);",
                "student_id");
        String sql = "INSERT INTO student(student_id, student_name, csat_score, school_records_score) "
                + "VALUES(?, ?, ?, ?);";

        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, studentId);
        preparedStatement.setString(2, studentName.length() < 20 ? studentName : studentName.substring(0, 20));
        preparedStatement.setInt(3, csatScore);
        preparedStatement.setInt(4, schoolRecordsScore);

        return preparedStatement.executeUpdate() == 1 ? true : false;
    }

    /* Function insert for application table */
    private static boolean insert(int studentId, int schoolId) throws SQLException {
        int applicationId = getRecordId(
                "SELECT application_id FROM application WHERE application_id in (SELECT max(application_id) FROM application);",
                "application_id");
        String sql = "INSERT INTO application(application_id, student_id, school_id) " + "VALUES(?, ?, ?);";

        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.setInt(2, studentId);
        preparedStatement.setInt(3, schoolId);

        return preparedStatement.executeUpdate() == 1 ? true : false;
    }

    private static boolean delete(String sql) throws SQLException {
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);

        return preparedStatement.executeUpdate() == 1 ? true : false;
    }

    private static boolean update(String sql) throws SQLException {
        PreparedStatement preparedStatement = SimpleDBA.getConnection().prepareStatement(sql);

        return preparedStatement.executeUpdate() == 1 ? true : false;
    }
}
