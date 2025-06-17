package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.ReviewDao;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Review;
import ssd.springcooler.gachiwatch.dto.ReviewDto;
import ssd.springcooler.gachiwatch.repository.MemberRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<ReviewDto> getReviewsByUser(int memberId) {
        List<Review> reviewList = reviewDao.getReviewsByUserId(memberId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewList) {
            Member member = memberRepository.findById(memberId).orElse(null);
            reviewDtos.add(new ReviewDto(Integer.toString(review.getReviewId()), Integer.toString(review.getContentId()),
                    review.getDate(), review.getSubstance(), Integer.toString(review.getScore()), Integer.toString(review.getLikes()), member.getNickname()));
        }
        return reviewDtos;
    }

    @Override
    public List<ReviewDto> getReviewsByContent(int memberId, int contentId) {
        List<Review> reviewList = reviewDao.getReviewsByContentId(contentId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewList) {
            if(memberId == review.getMember().getMemberId() && memberId != 0) { continue; }
            Member member = memberRepository.findById(review.getMember().getMemberId()).orElse(null);
            reviewDtos.add(new ReviewDto(Integer.toString(review.getReviewId()), Integer.toString(review.getContentId()),
                    review.getDate(), review.getSubstance(), Integer.toString(review.getScore()), Integer.toString(review.getLikes()), member.getNickname()));
        }
        return reviewDtos;
    }

    @Override
    public List<ReviewDto> getReviewsByContentAndUser(int memberId, int contentId) {
        List<Review> reviewList = reviewDao.getReviewsByContentIdAndMemberId(memberId, contentId);
        //아래 로직은 따로 메소드로 묶을까 고민중!!
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviewList) {
            Member member = memberRepository.findById(memberId).orElse(null);
            reviewDtos.add(new ReviewDto(Integer.toString(review.getReviewId()), Integer.toString(review.getContentId()),
                    review.getDate(), review.getSubstance(), Integer.toString(review.getScore()), Integer.toString(review.getLikes()), member.getNickname()));
        }
        return reviewDtos;
    }
    @Override
    public void deleteReview(int reviewId) {

    }

    @Override
    public void insertReview(int contentId, int memberId, String reviewContent, int star) {
        Date now = new Date(); // 현재 시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        String formatted = sdf.format(now);
        Member memberForId = new Member();
        memberForId.setMemberId(memberId);
        Review review = new Review(contentId, memberForId, reviewContent, star, formatted);
        reviewDao.insertReview(review);
    }

    @Override
    public void updateReview(int contentId, int memberId, String reviewContent, int star) {
        Date now = new Date(); // 현재 시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        String formatted = sdf.format(now);
        Member memberForId = new Member();
        memberForId.setMemberId(memberId);
        Review review = new Review(contentId, memberForId, reviewContent, star, formatted);
        reviewDao.updateReview(review);
    }
}
