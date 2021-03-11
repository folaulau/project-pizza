package com.lovemesomecoding.pizzaria.aws;

import java.io.File;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public interface AwsS3Service {

	/**
	 * 
	 * @param key
	 * @param file
	 * @return public file url
	 */
	String uploadFile(String key, File file);
}
