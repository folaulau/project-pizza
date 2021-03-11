package com.lovemesomecoding.pizzaria.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static List<File> convertMultipartFilesToFiles(List<MultipartFile> multipartFiles) throws IOException {
		return multipartFiles.stream().map(multipartFile -> {
			File convFile = null;
			try {
				convFile = new File(multipartFile.getOriginalFilename());
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(multipartFile.getBytes());
				fos.close();
			} catch (IOException e) {
			}
			return convFile;
		}).filter(file -> file != null).collect(Collectors.toList());
	}

	public static String getFileNameWithDateTime(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	/*
	 * Usecases: 1. Read LOB HTML check template from the file as LOB doesn't allow
	 * lots of custom templates. 2. Read SQL files from 'resources' directory
	 * instead of inline statements, if SQL statements spans over few lines.
	 */
	public static String getResourcesFileContentAsString(String resourcesFilePath) {
		String fileContents = null;

		try {
			InputStream resource = new ClassPathResource(resourcesFilePath).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
			fileContents = reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return fileContents;
	}
}
