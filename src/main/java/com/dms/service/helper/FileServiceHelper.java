package com.dms.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dms.config.constants.ConfigConstants;
import com.dms.data.model.FileMapping;
import com.dms.utils.CommonUtils;

import lombok.extern.log4j.Log4j2;
@Component
@Log4j2
public class FileServiceHelper {
	
	@Autowired private GridFsTemplate gridFsTemplate;
	@Autowired private CommonUtils commonUtils;
	@Autowired private ConfigConstants configConstants;

	@SuppressWarnings("static-access")
	public Integer saveFile(MultipartFile file) {
		try {
		String filepath=saveUploadFile(file);
		if(null !=filepath &&!filepath.isEmpty()) {
			InputStream inputStream = new FileInputStream(filepath);
			String docId= gridFsTemplate.store(inputStream,file.getOriginalFilename(), file.getContentType()).get().toString();
			FileMapping fileMapping = new FileMapping();
			fileMapping.setDocumentName(file.getOriginalFilename());
			fileMapping.setDocumnetType(file.getContentType());
			fileMapping.setDocId(docId);
			fileMapping.setRecordId(commonUtils.getNextSequenceId("File_Mapping",FileMapping.class));
			commonUtils.save(fileMapping);
			return fileMapping.getRecordId().intValue();
		}
		}catch(Exception e) {
			
		}
		return null;
	}

	private String saveUploadFile(MultipartFile file) {
		try {
			if (null != file && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				String filePath = configConstants.getFilePath() + file.getOriginalFilename();
				File create=new File(configConstants.getFilePath());
				if(!create.exists()){
					create.mkdirs();
				}
				Path path = Paths.get(filePath);
				Files.write(path, bytes);
				return filePath;
			}
		} catch (Exception e) {
			log.error("An Error occurred while saving file::", e);
		}
		return null;
	}

}
