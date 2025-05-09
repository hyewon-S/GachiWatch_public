package ssd.springcooler.gachiwatch.domain;//콘텐츠에 관한 정보

import java.util.Date;
import java.util.List;

public class Content {
	
	private int contentId;
	private String title;
	private String intro;
	private Genre genre;
	private List<String> cast; //출연진
	private Platform platform;
	private double rate; //평점
	private List<Review> reviews;
	private int views; //조회수
	private Date uploadDate; //업로드 날짜
	private String contentType;
	
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

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public List<String> getCast() {
		return cast;
	}

	public void setCast(List<String> cast) {
		this.cast = cast;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setContentType(String contentType) { this.contentType = contentType; }

	public String getContentType() { return contentType; }
	
	@Override
	public String toString() {
		return "contentID : " + contentId + " title: " + title;
	}
}
