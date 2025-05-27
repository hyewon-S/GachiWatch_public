package ssd.springcooler.gachiwatch.service;

import org.springframework.stereotype.Service;
import ssd.springcooler.gachiwatch.dto.ReviewDto;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Override
    public List<ReviewDto> getReviewsByUser(int memberId) {
        return null;
    }

    @Override
    public void deleteReview(int reviewId) {

    }
}
