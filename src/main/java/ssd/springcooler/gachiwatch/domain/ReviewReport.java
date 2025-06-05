package ssd.springcooler.gachiwatch.domain;//리뷰 신고 정보

import java.util.Date;

public class ReviewReport {
	private int reportId;
	private int memberId; //신고한 사용자
	private int reviewId; //신고된 리뷰
	private String substance; //신고 사유
	private Date date; //신고 날짜
	
	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getSubstance() {
		return substance;
	}

	public void setSubstance(String substance) { this.substance = substance; }

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ID" + reportId + "content : " + substance;
	}
}
