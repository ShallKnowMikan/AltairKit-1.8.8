# AltairKit

  This project is meant to simplify minecraft plugin development.


# Installation

 1) You have to download the jar file and the AltairBuilder.exe file.
 
 2) Put the AltairBuilder and the jar in the same folder and then execute
    AltairBuilder.exe .
    
 4) Once it's done you will only have to put those dependencies in your maven
    configuration:
    ```xml
      <dependency>
            <groupId>dev.mikan</groupId>
            <artifactId>AltairKit</artifactId>
            <version>1.8.8</version>
      </dependency>
    ```

# Dependencies

  In order to correctly run an AltairKit application, you will need 
  spigot and google-gson dependecies.
    
  ```xml
        <dependency>
            <groupId>org.spigotmc</groupId>
            <artifactId>spigot</artifactId>
            <version>1.8.8-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
  <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
  ```
