package ssd.springcooler.gachiwatch.domain;//리뷰 신고 정보

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review_report")
public class ReviewReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reportId;

	@Column(name = "member_id")
	private int memberId;

	@Column(name = "review_id")
	private int reviewId;

	private String substance;

	@Column(name = "review_report_date")
	private Date date;


	@Override
	public String toString() {
		return "ID" + reportId + "content : " + substance;
	}
}
