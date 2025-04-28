//리뷰 신고 정보
import java.util.Date;

public class ReviewReport {
	private int reportId;
	private Member reportMember; //신고한 사용자
	private Review review; //신고된 리뷰
	private String content; //신고 사유
	private Date date; //신고 날짜
	
	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public Member getReportMember() {
		return reportMember;
	}

	public void setReportMember(Member reportMember) {
		this.reportMember = reportMember;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ID" + reportId + "content : " + content;
	}
}
