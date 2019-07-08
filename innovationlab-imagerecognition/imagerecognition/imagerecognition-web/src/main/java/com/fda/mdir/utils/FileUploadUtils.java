package com.fda.mdir.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class FileUploadUtils {
	
	/**
	 * This method will create file under given parent directory with provided file name.
	 * <br />
	 * If parent directory structure not exist it will create it.
	 * <br />
	 * If file already exist, it will be delete old file and create new blank file.
	 * 
	 * @param relativeDirPath - relative path to parent directory, If null file will be created under root directory
	 * @param fileName - File Name for file to create
	 * @return - Created file
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static File createFileForMediaInRepo(String relativeDirPath, String fileName) 
			throws IOException, URISyntaxException {
        		
		File fileToCreate = new File(relativeDirPath, fileName);
		// delete of file already exist
		if(fileToCreate.exists()) {
			fileToCreate.delete();
		}
		fileToCreate.createNewFile();
		
		return fileToCreate;
	}
	
	public static void deleteFileForMediaRepo(String relativeDirPath, String fileName) {
		File fileToCreate = new File(relativeDirPath, fileName);
		
		if(fileToCreate.exists()) {
			fileToCreate.delete();
		}
	}
	
}
