package ssd.springcooler.gachiwatch.domain;//콘텐츠에 관한 정보

import java.util.Date;
import java.util.List;

public class Content {
	
	private int contentId;
	private String title;
	private String intro;
	private List<Integer> genre;
	private String cast; //출연진
	private List<Integer> platform;
	private double rate; //평점
	private Date uploadDate; //업로드 날짜
	private String contentType;
	private String imgUrl;
	
	//getters & setters
	
	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public List<Integer> getGenre() {
		return genre;
	}

	public void setGenre(List<Integer> genre) {
		this.genre = genre;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public List<Integer> getPlatform() {
		return platform;
	}

	public void setPlatform(List<Integer> platform) {
		this.platform = platform;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setContentType(String contentType) { this.contentType = contentType; }

	public String getContentType() { return contentType; }

	public String getImgUrl() { return imgUrl; }

	public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
	
	@Override
	public String toString() {
		return "contentID : " + contentId + " title: " + title;
	}
}
