package com.dms.config.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Service
public class ConfigConstants {
	@Value("${file.path}")
	private String filePath;

}
