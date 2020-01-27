package com.dms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dms.response.ApiResponse;
import com.dms.response.UploadResponse;
import com.dms.service.FileService;

@RestController
public class FileController {
	
	@Autowired FileService fileService;
	
	@PostMapping("/uploadFile")
	public ApiResponse<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		List<MultipartFile> multipleFiles= new ArrayList<>(Arrays.asList(file));
		return fileService.uploadFiles(multipleFiles);	
	}
	
	@PostMapping("/uploadMultipleFiles")
	public ApiResponse<UploadResponse> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) {
		List<MultipartFile> multipleFiles= new ArrayList<>(Arrays.asList(files));
		return fileService.uploadFiles(multipleFiles);	
	}
	
	@GetMapping("downloadFile/{docId}")
	public ApiResponse<UploadResponse> downloadFile(@PathVariable Integer docId,HttpServletResponse serveltResponse) {
		return fileService.downloadFile(docId,serveltResponse);
		
	}
}
