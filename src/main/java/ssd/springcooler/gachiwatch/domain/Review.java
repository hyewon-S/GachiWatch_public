package ssd.springcooler.gachiwatch.domain;//리뷰에 관한 정보

import java.util.Date;
import java.util.List;

public class Review {
	private int reviewId;
	private int memberId;
	private int contentId;
	private String substance; //리뷰 내용
	private int likes; //해댱 리뷰에 대한 좋아요 개수
	private Date date; //작성일, 최종 수정일
	private int score; //별점
	
	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getSubstance() {
		return substance;
	}

	public void setSubstance(String substance) {
		this.substance = substance;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "reviewId : " + reviewId + " contents : " + substance;
	}
	
}
