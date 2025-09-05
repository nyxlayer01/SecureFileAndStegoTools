Secure File Storage System with AES – Project Report

->Introduction
With the increasing use of digital data, securing sensitive files is a critical concern. This project focuses on creating a local file encryption/decryption system using AES-256. The system allows users to encrypt any file, ensuring that only someone with the correct password can decrypt it, protecting confidentiality and preventing unauthorized access.

->Abstract
This project implements a password-based AES-256 encryption system that can secure files locally. The program is developed in Java, and it works for any file type including text, images, and PDFs. Users provide a password, which is used to generate the AES key. The system allows encryption and decryption through a simple command-line interface. The key benefit is that the same password must be used for decryption, making it secure and straightforward.

->Tools Used
- Java: Programming language for development
- JDK: Java Development Kit for compiling and running code
- Java Cryptography Extension (JCE): For AES encryption/decryption
- Command-Line Interface (CLI): For simple interaction with the program

->Steps Involved in Building the Project
1. AES Key Generation from Password
   - The user provides a password.
   - A SHA-256 hash of the password is used to create a 256-bit AES key.

2. File Encryption
   - The program reads the input file as a byte stream.
   - AES-256 encryption is applied to the byte stream.
   - Encrypted content is saved as a new file with .enc extension.

3. File Decryption
   - The program reads the .enc file.
   - Using the password-generated key, the system decrypts the content.
   - Decrypted content is saved with _decrypted appended to the original filename.

4. Command-Line Interface
   - User selects encrypt or decrypt option.
   - User enters file path and password.
   - The program executes the selected operation and displays confirmation messages.

5. Testing
   - Multiple files of different types were encrypted and decrypted successfully.
   - Password mismatch prevented decryption, ensuring security.

->Conclusion
The Secure File Storage System demonstrates a practical implementation of AES-256 encryption for local file protection. It is simple yet effective, allowing users to encrypt and decrypt files with a password. The system is portable, works across different file types, and ensures that sensitive data remains secure. This project highlights the importance of cryptography in everyday digital security and provides a foundation for more advanced secure storage solutions in the future.

