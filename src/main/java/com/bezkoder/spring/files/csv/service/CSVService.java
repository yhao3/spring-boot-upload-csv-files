package com.bezkoder.spring.files.csv.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.files.csv.helper.CSVHelper;
import com.bezkoder.spring.files.csv.model.Bankcoded;
import com.bezkoder.spring.files.csv.model.Tutorial;
import com.bezkoder.spring.files.csv.repository.BankcodedRepository;
import com.bezkoder.spring.files.csv.repository.TutorialRepository;

@Service
public class CSVService {
  @Autowired
  TutorialRepository repository;

  @Autowired
  private BankcodedRepository bankcodedRepository;

  public void save(MultipartFile file) {
    try {
      List<Tutorial> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public void saveInputStream(InputStream in) {
    List<Tutorial> tutorials = CSVHelper.csvToTutorials(in);
    repository.saveAll(tutorials);
  }

  public void saveInputStreamForBankcode(InputStream in) {
    List<Bankcoded> bankcodeds = CSVHelper.csvToBankcodeds(in);
    bankcodedRepository.saveAll(bankcodeds);
  }

  public ByteArrayInputStream load() {
    List<Tutorial> tutorials = repository.findAll();

    ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
    return in;
  }

  public List<Tutorial> getAllTutorials() {
    return repository.findAll();
  }
}
