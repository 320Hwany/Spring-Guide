# Uploading Files

## What We Will Build

파일 업로드를 할 수 있는 스프링 웹 애플리케이션을 만들어보겠습니다.        

## Let's Do it

Dependencies는 Spring Web과 Thymeleaf을 선택해줍니다.   
스프링부트를 사용하면 스프링 MVC의 자동 구성으로 인해 파일 업로드를 위한 MultipartConfigElement이 스프링 빈으로   
등록됩니다.      

### StorageProperties

ConfigurationProperties를 사용하여 상태를 관리할 수 있습니다.    
@EnableConfigurationProperties(StorageProperties.class)과 같이 설정을 @SpringBootApplication과 같이   
작성하거나 별도의 config 클래스를 만들어줘야 합니다.   

````java
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "/Users/jeong-youhwan/Desktop/upload-test";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
````

### FileSystemStorageService - 파일 업로드

파일을 업로드 하기 위해 경로와 파일명을 합쳐 최종적으로 destinationFile을 Path로 받은 후    
security check를 진행한 후 Files의 copy() 메소드로 해당 경로에 파일을 업로드합니다.     

````java
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    ...

}
````

### FileSystemStorageService - 파일 가져오기

루트 경로와 파일 이름으로 해당 파일의 경로를 찾은 후 경로에 해당 파일이 있는 지 찾습니다.    
파일이 존재한다면 파일을 반환하고 존재하지 않으면 예외를 반환합니다.     

````java
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    ...

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    ...
}
````

[GUIDE 5 - 예제 코드](https://github.com/320Hwany/spring-guide/tree/main/guide5/src/main/java/guide5)            