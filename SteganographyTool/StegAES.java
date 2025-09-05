import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.security.*;

public class StegAES extends JFrame {

    private SecretKey secretKey;
    private File imageFile;
    private JTextArea textArea;

    public StegAES() {
        setTitle("AES Steganography Tool");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton chooseImg = new JButton("Choose Image");
        JButton encodeBtn = new JButton("Encode Text");
        JButton decodeBtn = new JButton("Decode Text");
        textArea = new JTextArea(10, 40);

        add(chooseImg);
        add(encodeBtn);
        add(decodeBtn);
        add(new JScrollPane(textArea));

        // Ask for password once and generate AES key
        try {
            String password = JOptionPane.showInputDialog("Enter password for encryption:");
            if (password == null || password.isEmpty()) password = "default";
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha.digest(password.getBytes("UTF-8"));
            secretKey = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Choose image
        chooseImg.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imageFile = chooser.getSelectedFile();
                textArea.setText("Selected: " + imageFile.getName());
            }
        });

        // Encode text
        encodeBtn.addActionListener((ActionEvent e) -> {
            if (imageFile == null) {
                JOptionPane.showMessageDialog(this, "Select an image first!");
                return;
            }
            try {
                String message = JOptionPane.showInputDialog("Enter message to hide:");
                if (message == null || message.isEmpty()) return;
                byte[] encrypted = encrypt(message.getBytes());
                String outputPath = imageFile.getParent() + "/stego.png";
                encodeLSB(encrypted, imageFile.getAbsolutePath(), outputPath);
                textArea.append("\nMessage hidden in stego.png");
                JOptionPane.showMessageDialog(this, "Encoding complete!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Decode text
        decodeBtn.addActionListener((ActionEvent e) -> {
            if (imageFile == null) {
                JOptionPane.showMessageDialog(this, "Select an image first!");
                return;
            }
            try {
                String stegoFile = imageFile.getParent() + "/stego.png";
                File f = new File(stegoFile);
                if (!f.exists()) {
                    JOptionPane.showMessageDialog(this, "No stego.png found!");
                    return;
                }
                byte[] hidden = decodeLSB(stegoFile);
                byte[] decrypted = decrypt(hidden);
                textArea.append("\nDecoded: " + new String(decrypted));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Decoding failed: " + ex.getMessage());
            }
        });
    }

    // AES Encryption
    private byte[] encrypt(byte[] data) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, secretKey);
        return c.doFinal(data);
    }

    private byte[] decrypt(byte[] data) throws Exception {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, secretKey);
        return c.doFinal(data);
    }

    // LSB Encoding with length header
    private void encodeLSB(byte[] data, String src, String dest) throws Exception {
        BufferedImage img = ImageIO.read(new File(src));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // prepend 4-byte length
        baos.write((data.length >> 24) & 0xFF);
        baos.write((data.length >> 16) & 0xFF);
        baos.write((data.length >> 8) & 0xFF);
        baos.write(data.length & 0xFF);
        baos.write(data);

        byte[] fullData = baos.toByteArray();
        int dataIndex = 0, bitIndex = 0;

        outer: for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (dataIndex >= fullData.length) break outer;
                int rgb = img.getRGB(x, y);
                int bit = (fullData[dataIndex] >> (7 - bitIndex)) & 1;
                rgb = (rgb & 0xFFFFFFFE) | bit;
                img.setRGB(x, y, rgb);

                if (++bitIndex == 8) {
                    bitIndex = 0;
                    dataIndex++;
                }
            }
        }
        ImageIO.write(img, "png", new File(dest));
    }

    // LSB Decoding with length header
    private byte[] decodeLSB(String src) throws Exception {
        BufferedImage img = ImageIO.read(new File(src));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int data = 0, bitCount = 0;
        byte[] header = new byte[4];
        int headerIndex = 0;
        int length = -1;

        outer: for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int bit = img.getRGB(x, y) & 1;
                data = (data << 1) | bit;
                if (++bitCount == 8) {
                    if (length == -1) {
                        header[headerIndex++] = (byte)data;
                        if (headerIndex == 4) {
                            length = ((header[0]&0xFF)<<24)|((header[1]&0xFF)<<16)|((header[2]&0xFF)<<8)|(header[3]&0xFF);
                        }
                    } else {
                        baos.write(data);
                        if (baos.size() >= length) break outer;
                    }
                    bitCount = 0;
                    data = 0;
                }
            }
        }
        return baos.toByteArray();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StegAES().setVisible(true));
    }
}
