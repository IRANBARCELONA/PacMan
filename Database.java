import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Database {
    private final String url = "jdbc:postgresql://db.asvzbndbvajbgturvobl.supabase.co:5432/postgres?sslmode=require";
    private final String user = "postgres";
    private final String password = "13850121p";


    public static void main(String[] args) {
        Database db = new Database();
        System.out.println(db.getGameByUsername("Wictor"));
    }

    public String getGameByUsername(String username) {
        String sql = "SELECT id, username, password, created_at, last_login, high_score, last_score, total_kills, play_time FROM game WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("username");
                String password = rs.getString("password");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp lastLogin = rs.getTimestamp("last_login");
                int highScore = rs.getInt("high_score");
                int lastScore = rs.getInt("last_score");
                int totalKill = rs.getInt("total_kills");
                int playTime = rs.getInt("play_time");



            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // اگر کاربر پیدا نشد
    }
}
