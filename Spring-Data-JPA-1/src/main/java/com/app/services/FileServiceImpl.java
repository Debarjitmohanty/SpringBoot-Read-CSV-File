package com.app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.User;
import com.app.repository.UserRepository;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private UserRepository repository;

	@Override
	public boolean hasCsvFormat(MultipartFile file) {
		String type = "text/csv";
		if (!type.equals(file.getContentType()))
			return false;
		return true;
	}

	@Override
	public void processAndSaveData(MultipartFile file) {
		try {
			List<User> users = csvToUsers(file.getInputStream());
			repository.saveAll(users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<User> csvToUsers(InputStream inputStream) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<User> users = new ArrayList<>();
			List<CSVRecord> records = csvParser.getRecords();
			for (CSVRecord csvRecord : records) {
				User user = new User(Long.parseLong(csvRecord.get("Index")), csvRecord.get("Height(Inches)"),
						csvRecord.get("Weight(Pounds)"));
				users.add(user);
			}
			return users;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
