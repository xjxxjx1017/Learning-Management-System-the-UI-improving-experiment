package com.xjxxjx1017.utils;

import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"content"})
public class FileMeta {
	
	private String name;
	private String size;
	private String url;
	private String thumbnailUrl;
	private String deleteUrl;
	private String deleteType = "DELETE";
	
	private String twitter;
	private int article_id;

	public int getArticle_id()
	{
		return article_id;
	}

	public void setArticle_id( int article_id )
	{
		this.article_id = article_id;
	}

	private String type;
	private InputStream content;
	
	private String fileIdInDatabase;
	
	public String getFileIdInDatabase() {
		return fileIdInDatabase;
	}

	public void setFileIdInDatabase(String fileIdInDatabase) {
		this.fileIdInDatabase = fileIdInDatabase;
		this.url = "fileservlet?type=download" + "&id=" + fileIdInDatabase;
		this.deleteUrl = "fileservlet?type=delete" + "&id=" + fileIdInDatabase;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	@Override
	public String toString() {
		return "FileMeta [name=" + name + ", size=" + size + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl
				+ ", deleteUrl=" + deleteUrl + ", deleteType=" + deleteType + ", twitter=" + twitter + ", type=" + type
				+ ", content=" + content + ", articleId=" + article_id + ", fileIdInDatabase=" + fileIdInDatabase + "]";
	}
	
//  "name": "picture1.jpg",
//  "size": 902604,
//  "url": "http:\/\/example.org\/files\/picture1.jpg",
//  "thumbnailUrl": "http:\/\/example.org\/files\/thumbnail\/picture1.jpg",
//  "deleteUrl": "http:\/\/example.org\/files\/picture1.jpg",
//  "deleteType": "DELETE"
	
}
