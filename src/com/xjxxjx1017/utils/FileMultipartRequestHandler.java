package com.xjxxjx1017.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

public class FileMultipartRequestHandler {

	public static List<FileMeta> uploadByApacheFileUpload(HttpServletRequest request) throws IOException, ServletException{
				
		List<FileMeta> files = new LinkedList<FileMeta>();
		
		// 1. Check request has multipart content
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileMeta temp = null;
		
		// 2. If yes (it has multipart "files")
		if(isMultipart){

			// 2.1 instantiate Apache FileUpload classes
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			

			// 2.2 Parse the request
			try {
				
				// 2.3 Get all uploaded FileItem
				List<FileItem> items = upload.parseRequest(request);
				String twitter = "";
				String article_id = "";
				
				// 2.4 Go over each FileItem
				for(FileItem item:items){
					
					// 2.5 if FileItem is not of type "file"
				    if (item.isFormField()) {

				    	// 2.6 Search for "twitter" parameter
				        if(item.getFieldName().equals("twitter"))
				        	twitter = item.getString();

				        if(item.getFieldName().equals("article_id"))
				        	article_id = item.getString( );
				    } else {
				       
				    	// 2.7 Create FileMeta object
				    	temp = new FileMeta();
						temp.setName(item.getName());
						temp.setContent(item.getInputStream());
						temp.setType(item.getContentType());
						temp.setSize(item.getSize()/1024+ "Kb");
						
				    	// 2.7 Add created FileMeta object to List<FileMeta> files
						files.add(temp);
				    }
				}
				
				// 2.8 Set "twitter" parameter 
				for(FileMeta fm:files){
					fm.setTwitter(twitter);
					fm.setArticle_id( Integer.parseInt( article_id ) );
				}
				
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		return files;
	}
}
