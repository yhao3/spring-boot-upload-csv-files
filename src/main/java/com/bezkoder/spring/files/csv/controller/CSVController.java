package com.bezkoder.spring.files.csv.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bezkoder.spring.files.csv.service.CSVService;

import javassist.tools.web.BadHttpRequest;

import com.bezkoder.spring.files.csv.helper.CSVHelper;
import com.bezkoder.spring.files.csv.message.ResponseMessage;
import com.bezkoder.spring.files.csv.model.Tutorial;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

  Logger log = LoggerFactory.getLogger(CSVController.class);

  @Autowired
  CSVService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @PostMapping("/uploadFromProject")
  public ResponseEntity<ResponseMessage> uploadFileFromProject() {
    String message = "";

    File srcFile = null;
    try {
      srcFile = ResourceUtils.getFile("classpath:test.csv");
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }

    log.info("\n=====\nsrcFile: {}\n=====\n", srcFile);
    
    // DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("R2_Location.csv", MediaType.ALL_VALUE, true, srcFile.getName());

    FileInputStream fis = null;
    try {
      fis = new FileInputStream(srcFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // MultipartFile mFile = new CommonsMultipartFile(fileItem);

    fileService.saveInputStream(fis);

    message = "Uploaded the file successfully: " + srcFile.getName();
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    // message = "Please upload a csv file!";
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @PostMapping("/uploadFromProject_bankcoded")
  public ResponseEntity<ResponseMessage> uploadFileFromProject_bankcoded() {
    String message = "";

    URL url = null;
    InputStream is = null;
    try {
      url = new URL("https://www.fisc.com.tw/TC/OPENDATA/R2_Location.csv");
      is = url.openStream();
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    log.info("\n=====\nis: {}\n=====\n", is);
    
    fileService.saveInputStreamForBankcode(is);
    message = "Uploaded the file successfully: R2_Location.csv";

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
  }

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials() {
    try {
      List<Tutorial> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "tutorials.csv";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }

}
