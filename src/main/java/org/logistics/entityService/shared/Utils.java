package org.logistics.entityService.shared;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class Utils {

    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public String getEncryptedValue(String value, String algorithType) {
        String encryptedValue = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithType);
            md.update(value.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedValue = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }

    public String getValueForSpanClass(String htmlString, String className) {
        Document doc = Jsoup.parse(htmlString);
        String returnValue = doc.select("span[class=" + className + "]").text();
        return returnValue.length() == 0 ? null : returnValue;
    }
}
