import java.util.*;

public class EandDcrypt {

    private final List<Character> baseList;
    private final List<Character> ShuffleList;

    public EandDcrypt() {
        baseList = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) baseList.add(c);
        for (char c = 'A'; c <= 'Z'; c++) baseList.add(c);
        for (char c = '0'; c <= '9'; c++) baseList.add(c);
        String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>/?`~\"\\";
        for (char c : specialChars.toCharArray()) baseList.add(c);
        ShuffleList = new ArrayList<>(baseList);
        Collections.shuffle(ShuffleList);
    }

    public String encrypt(String password) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : password.toCharArray()) {
            for (int i = 0; i < baseList.size(); i++) {
                if (c == baseList.get(i)) encrypted.append(ShuffleList.get(i));
            }
        }
        return encrypted.toString();
    }


    public String decrypt(String encryptedText) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            for (int i = 0; i < ShuffleList.size(); i++) {
                if (c == ShuffleList.get(i)) decrypted.append(baseList.get(i));
            }
        }
        return decrypted.toString();
    }
}

