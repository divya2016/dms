package com.dms.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dms.config.constants.CommonConstants;
import com.dms.data.model.FileMapping;
import com.dms.response.ApiResponse;
import com.dms.response.FileResponse;
import com.dms.response.UploadResponse;
import com.dms.service.FileService;
import com.dms.service.helper.FileServiceHelper;
import com.dms.utils.CommonUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

	@Autowired
	FileServiceHelper fileServiceHelper;
	@Autowired
	CommonUtils commonUtils;
	@Autowired
	GridFsTemplate gridTemplate;
	@Autowired GridFSBucket gridFSBucket;

	@Override
	public ApiResponse<UploadResponse> uploadFiles(List<MultipartFile> multipleFiles) {
		if (null != multipleFiles && !multipleFiles.isEmpty()) {
			List<FileResponse> fileResponseList = new ArrayList<>();
			for (MultipartFile file : multipleFiles) {
				Integer docId = fileServiceHelper.saveFile(file);
				log.info("docId is ::" + docId);
				fileResponseList.add(new FileResponse(file.getName(), docId));
			}
			UploadResponse response = new UploadResponse(fileResponseList);
			return new ApiResponse<>(response, CommonConstants.SUCCESS, "File Upload Successfully.");
		}
		return null;
	}

	@Override
	public ApiResponse<UploadResponse> downloadFile(Integer docId, HttpServletResponse serveltResponse){
		try {
		if (null != docId) {
			Map<String, Object> params = new HashMap<>();
			params.put("recordId", docId);
			FileMapping fileMapping = commonUtils.findOne(params, FileMapping.class);
			if (null != fileMapping) {
				GridFSFile gridFile = gridTemplate.findOne(new Query(Criteria.where("_id").is(fileMapping.getDocId())));
				GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFile.getObjectId());
				GridFsResource gridFsResource = new GridFsResource(gridFile,gridFSDownloadStream );
				if (null != gridFile) {
					String contentType = URLConnection.guessContentTypeFromName(fileMapping.getDocumentName());
					if (contentType == null) {
						contentType = "application/octet-stream";
					}
					serveltResponse.setContentType(contentType);
					serveltResponse.setHeader("Content-Disposition",
							String.format("inline; filename=\"" + fileMapping.getDocumentName() + "\""));
					serveltResponse.setContentLength((int)gridFile.getLength());
					OutputStream out = serveltResponse.getOutputStream();
					InputStream in = gridFsResource.getInputStream();
					byte[] buffer = new byte[1048];         
		            int numBytesRead;
		            while ((numBytesRead = in.read(buffer)) > 0) {
		                out.write(buffer, 0, numBytesRead);
		            }
				}
			}

		}
		}catch(Exception e) {
			
		}
		return null;
	}

}
