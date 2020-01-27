package com.dms.data.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "File_Mapping")
public class FileMapping {
	private Long recordId;
	private String docId;
	private String documentName;
	private String documnetType;
}
