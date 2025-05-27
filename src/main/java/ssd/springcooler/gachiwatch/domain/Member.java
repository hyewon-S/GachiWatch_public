package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter // lombok 어노테이션 (모든 필드에 적용)
@Entity
@Table(name = "Member")
public class Member {
    @Id
    @SequenceGenerator(
            name = "member_seq_gen",
            sequenceName = "member_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")

    private int memberId; // 멤버 고유 ID

    private String name; // 이름
    private String password; // 비밀번호
    private String email; // 이메일
    private String nickname; // 닉네임
    private String profileImage; // 프로필 사진

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별 (하나만 선택이라 list타입 할 필요 x)
    private LocalDate birthdate; // 생년월일


    @ElementCollection
    @CollectionTable(name = "member_subscribed_ott", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private List<Platform> subscribedOTTs = new ArrayList<>(); // 구독 중인 OTT 리스트

    @ElementCollection
    @CollectionTable(name = "member_preferred_genre", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private List<Genre> preferredGenres = new ArrayList<>(); // // 선호 장르 리스트

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    //Review 클래스에서는 이렇게 설정돼 있어야 함
    //@ManyToOne
    //@JoinColumn(name = "member_id")
    //private Member member;
    private List<Review> reviews = new ArrayList<>(); // 작성한 리뷰 리스트

    @ManyToMany
    @JoinTable(
            name = "member_liked_content",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Content> likedContents = new ArrayList<>(); // 찜했어요 콘텐츠 목록

    @ManyToMany
    @JoinTable(
            name = "member_watched_content",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Content> watchedContents = new ArrayList<>(); // 봤어요 콘텐츠 목록

    @ManyToMany //(mappedBy = "members")
    @JoinTable(
            name = "member_crew",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "crew_id")
    )
    private List<Crew> joinedCrews = new ArrayList<>(); // 참여중인 크루 목록

    // 기본 생성자
    public Member() {}

    // 사용자 지정 생성자
    public Member(String name, String password, String email, String nickname, Gender gender, LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.birthdate = birthdate;
    }
/*
    // Getter & Setter 어노테이션으로 대체

    // 프로필 이미지 수정
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    // 닉네임 수정
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    // 비밀번호 수정
    public void updatePassword(String password) {
        this.password = password;
    }

    // 구독 서비스 수정
    public void updateSubscribedOTTs(List<Platform> ottList) {
        this.subscribedOTTs = new ArrayList<>(ottList);
    }

    // 선호 장르 수정
    public void updatePreferredGenres(List<Genre> genres) {
        this.preferredGenres = new ArrayList<>(genres);
    }

    // 시청한 콘텐츠 삭제
    public void removeWatchedContents(Content content) {
        watchedContents.remove(content);
    }

    // 리뷰 삭제
    public void deleteReviews(Review review) {
        reviews.remove(review);
    }
*/
}