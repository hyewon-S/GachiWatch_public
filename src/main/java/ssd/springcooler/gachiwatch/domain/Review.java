package ssd.springcooler.gachiwatch.domain;//리뷰에 관한 정보

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
	@Id
	private int reviewId;
	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;
	@Column(name = "content_id")
	private int contentId;
	private String substance; //리뷰 내용
	private int likes; //해댱 리뷰에 대한 좋아요 개수
	@Column(name = "review_date")
	private String date; //작성일, 최종 수정일
	private int score; //별점

	@ManyToOne
	@JoinColumn(name = "content_id", referencedColumnName = "content_id", insertable = false, updatable = false, nullable = false)
	private Content content;


	public Review(int contentId, Member member, String substance, int score, String date) {
		this.contentId = contentId;
		this.member = member;
		this.substance = substance;
		this.score = score;
		this.date = date;
	}

	@Override
	public String toString() {
		return "reviewId : " + reviewId + " contents : " + substance + "memberId : " + member.getMemberId() + "contentId : " + contentId + "date : " + date;
	}

}
