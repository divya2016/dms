package com.dms.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.dms.response.ApiResponse;
import com.dms.response.UploadResponse;

public interface FileService {

	public ApiResponse<UploadResponse> uploadFiles(List<MultipartFile> multipleFiles);

	public ApiResponse<UploadResponse> downloadFile(Integer docId, HttpServletResponse serveltResponse);

}
