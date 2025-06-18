package ssd.springcooler.gachiwatch.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dao.ReviewDao;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Review;
import ssd.springcooler.gachiwatch.domain.ReviewLike;
import ssd.springcooler.gachiwatch.dto.ReviewDto;
import ssd.springcooler.gachiwatch.repository.MemberRepository;
import ssd.springcooler.gachiwatch.repository.ReviewLikeRepository;

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

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Override
    public List<ReviewDto> getReviewsByUser(int memberId) {
        return commonLogicForReview(reviewDao.getReviewsByUserId(memberId), memberId);
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
        return commonLogicForReview(reviewDao.getReviewsByContentIdAndMemberId(memberId, contentId), memberId);
    }

    @Override
    public List<ReviewDto> commonLogicForReview(List<Review> reviewList, int memberId) {
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
        reviewDao.deleteReview(reviewId);
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
    public void updateReview(int reviewId, String reviewContent, int star) {
        Date now = new Date(); // 현재 시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        String formatted = sdf.format(now);
        reviewDao.updateReview(reviewId, reviewContent, star, formatted);
    }

    @Transactional
    @Override
    public void updateLiked(int reviewId, int memberId, int like, boolean isAdded) {
        reviewDao.updateReviewLike(reviewId, like);

        if(isAdded) {
            ReviewLike reviewLike = new ReviewLike(reviewId, memberId);
            reviewLikeRepository.save(reviewLike);
        } else {
            reviewLikeRepository.deleteByReviewIdAndMemberId(reviewId, memberId);
        }
    }

    @Override
    public String checkReviewHeart(int memberId) {
        boolean isLiked = reviewLikeRepository.existsByMemberId(memberId);
        String likeUrl = "/image/icon/icon-heart-black.png";
        if(isLiked) {
            likeUrl = "/image/icon/icon-heart-red.png";
        }
        return likeUrl;
    }
}
