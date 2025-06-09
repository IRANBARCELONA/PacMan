package Online;

import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class Database {
    private final String url = "jdbc:postgresql://ep-empty-salad-a81uz259-pooler.eastus2.azure.neon.tech/neondb?sslmode=require";
    private final String user = "neondb_owner";
    private final String password = "npg_UNFDxX8L6CkO";


    public GameUser getGameUserByUsername(String username) {
        String sql = "SELECT id, username, password, created_at, last_login, play_time, total_kills, high_score, last_score FROM game WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String uname = rs.getString("username");
                String pass = rs.getString("password");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp lastLogin = rs.getTimestamp("last_login");

                String playTimeStr = rs.getString("play_time");
                Duration playTime = parseIntervalToDuration(playTimeStr);

                int totalKills = rs.getInt("total_kills");
                int highScore = rs.getInt("high_score");
                int lastScore = rs.getInt("last_score");

                return new GameUser(id, uname, pass, createdAt, lastLogin, playTime, totalKills, highScore, lastScore);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Duration parseIntervalToDuration(String intervalStr) {
        try {
            long days = 0;
            String timePart = intervalStr;

            if (intervalStr.contains("day")) {
                String[] parts = intervalStr.split(" ");
                days = Long.parseLong(parts[0]);
                timePart = parts[2];
            }

            LocalTime time = LocalTime.parse(timePart);
            return Duration.ofDays(days)
                    .plusHours(time.getHour())
                    .plusMinutes(time.getMinute())
                    .plusSeconds(time.getSecond());

        } catch (Exception e) {
            e.printStackTrace();
            return Duration.ZERO;
        }
    }

    public boolean createUser(String username, String password) {
        String sql = "INSERT INTO game (username, password) " +
                "VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateGameUserById(GameUser user) {
        String sql = "UPDATE game SET password = ?, created_at = ?, last_login = ?, play_time = ?::interval, total_kills = ?, high_score = ?, last_score = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getPassword());
            pstmt.setTimestamp(2, user.getCreatedAt());
            pstmt.setTimestamp(3, user.getLastLogin());

            pstmt.setString(4, user.getPlayTime().toString());

            pstmt.setInt(5, user.getTotalKills());
            pstmt.setInt(6, user.getHighScore());
            pstmt.setInt(7, user.getLastScore());
            pstmt.setInt(8, user.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



}
