package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId; // 각각의 멤버에게 할당되는 id

    private String name;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthdate;

    @ElementCollection
    private List<String> subscribedOTT = new ArrayList<>();

    @ElementCollection
    private List<String> preferredGenres = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "member_liked_content",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Content> likedContents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "member_watched_content",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Content> watchedContents = new ArrayList<>();

    @ManyToMany(mappedBy = "member")
    private List<SubscriptionPod> joinedPods = new ArrayList<>();

    // 기본 생성자
    public Member() {}

    // 생성자
    public Member(String username, String password, String email, String nickname, Gender gender, LocalDate birthdate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    // Getter & Setter
    public String getUsername() { return username; }
    public void setEmail(String email) { this.email = email; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 프로필 이미지 변경
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 구독 서비스 변경
    public void updateSubscribedOTT(List<String> ottList) {
        this.subscribedOTT = new ArrayList<>(ottList);
    }

    // 선호 장르 변경
    public void updatePreferredGenres(List<String> genres) {
        this.preferredGenres = new ArrayList<>(genres);
    }

    // 찜한 콘텐츠 추가 및 삭제
    public void likeContent(Content content) {
        likedContents.add(content);
    }
    public void removeLikedContent(Content content) {
        likedContents.remove(content);
    }

    // 시청한 콘텐츠 추가 및 삭제
    public void watchContent(Content content) {
        watchedContents.add(content);
    }
    public void removeWatchedContent(Content content) {
        watchedContents.remove(content);
    }

    // 리뷰 작성 및 삭제
    public void writeReview(Review review) {
        reviews.add(review);
    }
    public void deleteReview(Review review) {
        reviews.remove(review);
    }
}

