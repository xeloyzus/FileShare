# FileShare

A Java-based peer-to-peer file sharing application that enables direct file transfers between devices on a local network without requiring internet connectivity or cloud services.

## 🚀 Features

- **Direct P2P File Transfer**: Share files directly between devices on the same local network
- **No Internet Required**: Works entirely offline over LAN connections
- **Rich File Type Support**: Handles multiple file formats including:
  - Documents (PDF, DOC, XLS, PPT, RTF, TXT)
  - Images (JPG, PNG, GIF, BMP, TIF, RAW, PSD, SVG)
  - Videos (AVI, FLV, MPG, MOV, DIVX)
  - Audio (MP3, WAV, WMA, AAC, FLAC)
  - Archives (ZIP, RAR, ISO)
  - And many more
- **User-Friendly GUI**: Intuitive Java Swing-based graphical interface with modern Material Design-inspired theme
- **File Preview Icons**: Visual representation of different file types for easy identification
- **Progress Tracking**: Real-time upload/download progress monitoring
- **Connection Management**: Easy server/client connection setup with IP and port configuration
- **MAC Address Verification**: Device identification using MAC addresses for secure connections
- **Custom Listeners**: Event-driven architecture for upload progress, memory management, and exit handling

## 📋 Requirements

- **Java Runtime Environment (JRE)** 8 or higher
- **Java Development Kit (JDK)** 8 or higher (for building from source)
- Devices must be on the same local network (LAN)
- Network connectivity between devices

## 🔧 Dependencies

This project uses the following libraries:
- **Apache Commons IO**: File handling utilities
- **Apache Commons Lang3**: String manipulation and utilities
- **JSON Simple**: JSON parsing for network communication
- **QRGen**: QR code generation capabilities (optional feature)

## 📦 Installation

### Download Pre-built Application
1. Download the latest release from the [Releases](https://github.com/xeloyzus/FileShare/releases) page
2. Extract the contents to your preferred location
3. Ensure Java 8 or higher is installed on your system

### Build from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/xeloyzus/FileShare.git
   cd FileShare
   ```

2. Extract the required libraries:
   ```bash
   # Extract lib.zip and content.zip if not already extracted
   unzip lib.zip
   unzip content.zip
   ```

3. Compile the Java source files:
   ```bash
   javac -cp "lib/*:." -d bin src/project_a/*.java src/Custom_Listeners/*.java src/net/glxn/qrgen/**/*.java
   ```

4. Run the application:
   ```bash
   java -cp "bin:lib/*" project_a.MainInterface
   ```

## 🎯 Usage

### Starting as a Server (Sharing Files)

1. Launch the FileShare application
2. The application will automatically detect your local IP address
3. Select the files or folders you want to share
4. Share your IP address and port number with the receiving device
5. Wait for incoming connection requests

### Connecting as a Client (Receiving Files)

1. Launch the FileShare application
2. Click on "Connect to Server" option
3. Enter the server's IP address and port number
4. Browse available files from the connected server
5. Select files to download
6. Monitor download progress in real-time

## 🏗️ Architecture

### Project Structure

```
FileShare/
├── src/
│   ├── project_a/              # Main application code
│   │   ├── MainInterface.java  # Main GUI window
│   │   ├── Project_A.java      # Application entry point
│   │   ├── SendCommand.java    # Command sending logic
│   │   ├── RecieveCommand.java # Command receiving logic
│   │   ├── MyMethods.java      # Utility methods
│   │   ├── StaticValues.java   # Static constants and resources
│   │   └── Dialog*.java        # Dialog windows
│   ├── Custom_Listeners/       # Event listener interfaces
│   │   ├── Exit_Listener.java
│   │   ├── Upload_Listener.java
│   │   └── Memory_Threshold_Listener.java
│   ├── net/glxn/qrgen/        # QR code generation library
│   └── file_Extensions/        # File type definitions
├── lib.zip                     # Required libraries
├── content.zip                 # Icons and resources
└── README.md
```

### Key Components

- **MainInterface**: Primary GUI window for application interaction
- **SendCommand/RecieveCommand**: Handle network communication protocols
- **DialogConnectToServer**: Connection establishment dialog
- **JFrame_select_items**: File selection interface
- **Dialog_Download_Files**: Download progress tracking
- **Custom Listeners**: Event handling for uploads, exits, and memory management

## 🔌 Network Protocol

The application uses a custom JSON-based protocol over TCP sockets for communication:

- **Request_File**: Request specific files from server
- **Initiate_Connection**: Establish connection with server
- **Connection_Initiation_Response**: Server response to connection request
- **Confirm_File**: Verify file details before transfer
- **Confirmed_File_Results**: Server confirms file availability
- **Send_File**: Initiate file transfer

## 🎨 Themes

The application features a Material Design-inspired theme with customizable color schemes:
- Blue Material theme (default)
- Military theme option available

## 🔒 Security Considerations

- This application is designed for trusted local networks
- No encryption is implemented in the current version
- MAC address verification provides basic device identification
- Consider using a VPN or secure network for sensitive file transfers

## 🐛 Troubleshooting

### Cannot Connect to Server
- Ensure both devices are on the same network
- Check firewall settings and allow the application through
- Verify the IP address and port number are correct
- Ensure the server device is running and listening

### Files Not Showing
- Check file permissions on the sharing device
- Verify network connectivity between devices
- Restart the application and try again

### Slow Transfer Speeds
- Check network bandwidth and other network usage
- Reduce the number of simultaneous transfers
- Ensure devices are connected via wired connection if possible

## 📝 Version

Current Version: **1.1**

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## 📄 License

This project is available for use and modification. Please check with the repository owner for specific licensing terms.

## 👤 Author

**vian** (xeloyzus)

## 📧 Contact

For questions, issues, or suggestions, please open an issue on the [GitHub repository](https://github.com/xeloyzus/FileShare/issues).

---

**Note**: This is a peer-to-peer file sharing application intended for use on trusted local networks. Users are responsible for ensuring compliance with applicable laws and regulations regarding file sharing.
