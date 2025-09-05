import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.*;

public class SecureFileStorage {

    private static SecretKey secretKey;

    // Generate AES key from user password
    private static void generateKeyFromPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(password.getBytes("UTF-8"));
        secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypt a file
    private static void encryptFile(File inputFile, File outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
        }
        System.out.println("Encryption complete: " + outputFile.getName());
    }

    // Decrypt a file
    private static void decryptFile(File inputFile, File outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(inputFile);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
        }
        System.out.println("Decryption complete: " + outputFile.getName());
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Secure File Storage System with AES-256");
            System.out.println("1. Encrypt a file");
            System.out.println("2. Decrypt a file");
            System.out.print("Choose option (1/2): ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter file path: ");
            String inputPath = sc.nextLine();
            File inputFile = new File(inputPath);

            if (!inputFile.exists()) {
                System.out.println("File not found!");
                return;
            }

            System.out.print("Enter a password (use the same for decrypting): ");
            String password = sc.nextLine();
            generateKeyFromPassword(password);

            if (choice == 1) {
                File encryptedFile = new File(inputFile.getParent(), inputFile.getName() + ".enc");
                encryptFile(inputFile, encryptedFile);
            } else if (choice == 2) {
                if (!inputPath.endsWith(".enc")) {
                    System.out.println("File is not a .enc file!");
                    return;
                }
                String originalName = inputFile.getName().replace(".enc", "_decrypted");
                File decryptedFile = new File(inputFile.getParent(), originalName);
                decryptFile(inputFile, decryptedFile);
            } else {
                System.out.println("Invalid choice!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
