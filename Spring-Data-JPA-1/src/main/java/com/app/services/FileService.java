package com.app.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

	boolean hasCsvFormat(MultipartFile file);

	void processAndSaveData(MultipartFile file);

}
