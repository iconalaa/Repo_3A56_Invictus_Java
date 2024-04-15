package tests;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Doctor;
import entities.Radiologist;
import services.user.RadiologistService;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;


public class Main {

    public static void main(String[] args) {
//            ******** cryPtage *********
        char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, "123456".toCharArray());
        StringBuilder sb = new StringBuilder();
        for (char c : bcryptChars) {
            sb.append(c);
        }
        String hashedPassword = sb.toString();
        BCrypt.Result result = BCrypt.verifyer().verify("123456".toCharArray(), hashedPassword);
        System.out.println(result.verified);
    }


}

