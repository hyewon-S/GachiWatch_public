package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter // lombok 어노테이션 (모든 필드에 적용)
@Setter
@Entity
@Builder
@Where(clause = "deleted = false")
public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "member_seq_gen",
            sequenceName = "member_seq",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
    @Column(name="member_id")
    private Integer memberId; // 멤버 고유 ID

    private String name; // 이름
    private String password; // 비밀번호
    private String email; // 이메일
    private String nickname; // 닉네임
//    private String profileImage; // 프로필 사진

    @Enumerated(EnumType.ORDINAL)
    @Column(name="gender_id")
    private Gender gender; // 성별 (하나만 선택이라 list타입 할 필요 x)

    @Temporal(TemporalType.DATE)
    @Column(name="birth_date")
    private LocalDate birthdate; // 생년월일

    @ElementCollection
    @CollectionTable(name = "member_subscribed_ott", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private List<Platform> subscribedOtts = new ArrayList<>(); // 구독 중인 OTT 리스트

    @ElementCollection
    @CollectionTable(name = "member_preferred_genre", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private List<Genre> preferredGenres = new ArrayList<>(); // // 선호 장르 리스트

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
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
            name = "joinedcrews",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "crew_id")
    )
    private List<Crew> joinedCrews = new ArrayList<>(); // 참여중인 크루 목록

    @Column(name = "deleted")
    private boolean deleted = false;


}