package liftheavy.com.wongmatthew.liftheavy;

import android.util.Log;

/**
 * Created by Matthew on 10/16/2017.
 */

public class GenerateID {
    private static int ID_LENGTH = 8;
    public static String generateID(){
        String randomStringPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i=0;i < ID_LENGTH;i++){
            int random = (int) (Math.random() * randomStringPool.length());
            Character c = randomStringPool.charAt(random);
            sb.append(c);
        }
        return sb.toString();
    }
}
