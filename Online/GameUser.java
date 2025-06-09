package Online;

import java.sql.Timestamp;
import java.time.Duration;

public class GameUser {
    private int id;
    private String username;
    private String password;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private Duration playTime;
    private int totalKills;
    private int highScore;
    private int lastScore;

    public GameUser(int id, String username, String password, Timestamp createdAt, Timestamp lastLogin,
                    Duration playTime, int totalKills, int highScore, int lastScore) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.playTime = playTime;
        this.totalKills = totalKills;
        this.highScore = highScore;
        this.lastScore = lastScore;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public Duration getPlayTime() {
        return playTime;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getLastScore() {
        return lastScore;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPlayTime(Duration playTime) {
        this.playTime = playTime;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    @Override
    public String toString() {
        return "GameUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                ", playTime=" + playTime +
                ", totalKills=" + totalKills +
                ", highScore=" + highScore +
                ", lastScore=" + lastScore +
                '}';
    }
}
