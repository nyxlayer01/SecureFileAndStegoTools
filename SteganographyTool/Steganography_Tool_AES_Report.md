Steganography Tool with AES – Project Report

->Introduction
Hiding sensitive information within digital media adds an extra layer of security. This project creates a steganography tool that hides messages in images using AES encryption. Only users with the correct password can extract and decrypt the hidden message.

->Abstract
The project implements a GUI-based tool in Java that embeds encrypted messages into images using Least Significant Bit (LSB) encoding. Messages are first encrypted with AES-256 using a password-derived key. Users can select images, hide messages, and later decode and decrypt them using the same password. This approach ensures both secrecy of content and protection against unauthorized access.

->Tools Used
- Java: Programming language for development
- Swing: GUI for user interaction
- Java Cryptography Extension (JCE): AES encryption/decryption
- javax.imageio: Image reading and writing
- File I/O: Handling image files

->Steps Involved in Building the Project
1. AES Key Generation from Password
   - User provides a password.
   - SHA-256 hash generates the 256-bit AES key.

2. Message Encryption
   - Input text message is converted to bytes.
   - AES-256 encrypts the byte array.

3. LSB Encoding
   - Encrypted bytes are embedded into the least significant bits of image pixels.
   - Encoded image is saved as stego.png.

4. Message Decoding
   - LSB data is extracted from the image.
   - AES decryption is applied using the same password to retrieve the original message.

5. GUI Interface
   - User selects an image and inputs a message to hide.
   - Encoded or decoded results are displayed in a text area.

6. Testing
   - Multiple messages successfully hidden and retrieved from different images.
   - Incorrect passwords prevent decryption, maintaining security.

->Conclusion
The Steganography Tool combines LSB image encoding with AES-256 encryption to securely hide and retrieve messages. It demonstrates an effective method of covert communication while ensuring confidentiality. The GUI simplifies interaction, making it accessible and practical for educational and small-scale security purposes.

