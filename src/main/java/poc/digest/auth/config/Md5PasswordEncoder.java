package poc.digest.auth.config;

import java.security.MessageDigest;

public class Md5PasswordEncoder {

  public String encode(String pass) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      final byte[] array = md.digest(pass.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : array) {
        sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
      }
      return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
      return "";
    }
  }

  public static void main(String[] args) {
    System.out.println("live: " + new Md5PasswordEncoder().encode("live:REALM:live"));
    System.out.println("local: " + new Md5PasswordEncoder().encode("local:REALM:local"));
    System.out.println("test: " + new Md5PasswordEncoder().encode("test:REALM:test"));
  }

}
