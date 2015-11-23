import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleDBA {
	private static final String SERVER_NAME = "147.46.15.238";
	private static final String DB_NAME = "DB-STUDENT-NUMBER";
	private static final String USER_NAME = "DB-STUDENT-NUMBER";
	private static final String PASSWORD = "DB-STUDENT-NUMBER";

	private static SimpleDBA instance;
	private static Connection connection;

	public static void main(String[] args) {
		instance = new SimpleDBA();
		instance.setup();

		while (true) {
			BufferedReader br = null;

			try {
				Message.print(Message.INTRODUCTION, null);
				br = new BufferedReader(new InputStreamReader(System.in));
				QueryHelper.command(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void setup() {
		final String URL = "jdbc:mariadb://" + "147.46.15.238" + "/" + "DB-STUDENT-NUMBER";

		try {
			connection = DriverManager.getConnection(URL, "DB-STUDENT-NUMBER", "DB-STUDENT-NUMBER");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}
