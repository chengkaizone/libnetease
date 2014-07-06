package org.libnetease.entity;

/**
 * 新闻模型
 * @author lance
 */
public class NewsInf {
	/**
	 * 图片预览地址
	 */
	private String preview;
	/**
	 * 新闻标题
	 */
	private String title;
	/**
	 * 新闻简介
	 */
	private String content;
	/**
	 * 回复
	 */
	private String review;
	
	public String getPreview() {
		return preview;
	}
	public void setPreview(String preview) {
		this.preview = preview;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
}
