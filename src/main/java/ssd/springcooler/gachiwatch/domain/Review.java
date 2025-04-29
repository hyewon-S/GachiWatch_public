package ssd.springcooler.gachiwatch.domain;//리뷰에 관한 정보
import ssd.springcooler.gachiwatch.domain.Member;

import java.util.Date;
import java.util.List;

public class Review {
	private int reviewId;
	private Member member; //Member 클래스 필요
	private Content content;
	private String contents; //리뷰 내용
	private int likes; //해댱 리뷰에 대한 좋아요 개수
	private Date date; //작성일, 최종 수정일
	private List<ReviewReport> reportList;
	
	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
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

	public List<ReviewReport> getReportList() {
		return reportList;
	}

	public void setReportList(List<ReviewReport> reportList) {
		this.reportList = reportList;
	}

	@Override
	public String toString() {
		return "reviewId : " + reviewId + " contents : " + contents;
	}
	
}
