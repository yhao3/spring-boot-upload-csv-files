# Spring Boot Upload/Download CSV Files with MySQL database example

For more detail, please visit:
> [Spring Boot: Upload CSV file to MySQL Database | Multipart File](https://bezkoder.com/spring-boot-upload-csv-file/)
> [Spring Boot learning/Section08-5/Upload CSV file to MySQL Database | Hao's Notion](https://www.notion.so/Section08-5-Uploading-and-Parsing-CSV-File-using-Spring-Boot-218f797f44e6479aa2b0308918a6e3ae)

## Run Spring Boot application
```
mvn spring-boot:run
```

## call API
Open API Tester
### choose a file manually
- METHOD: `POST`
- URL: `http://localhost:8080/api/csv/uploadFromProject`
- Request body: `file  [File]`  =  `【Choose a file from local...】`

### directly access a download URL read file
- METHOD: `POST`
- URL: `http://localhost:8080/api/csv/uploadFromProject_bankcoded`

