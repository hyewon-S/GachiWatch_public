package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crew_seq_gen")
    @SequenceGenerator(name = "crew_seq_gen", sequenceName = "CREW_SEQ", allocationSize = 1)
    @Column(name = "crew_id")
    Long crewId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    Member captain;

    @ManyToMany(mappedBy = "joinedCrews")
    List<Member> crewMembers = new ArrayList<Member>();
    String crewName;
    String crewDesc;
    @Enumerated(EnumType.STRING)
    Platform platform;
    int payment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date payDate;
    int maxMember;
    String account;

    public Crew(Member captain, String crewName, int maxMember) {
        this.captain = captain;
        this.crewName = crewName;
        this.maxMember = maxMember;
    }

    public Crew(Member captain, String crewName, String crewDesc, Platform platform, int payment,
                Date payDate, int maxMember, String account) {
        super();
        this.captain = captain;
        this.crewName = crewName;
        this.crewDesc = crewDesc;
        this.platform = platform;
        this.payment = payment;
        this.payDate = payDate;
        this.maxMember = maxMember;
        this.account = account;
    }

    public Crew() {

    }

    public int getCurrentCount() {
        return crewMembers.size();
    }

    public int calcPayFee() {
        return payment / crewMembers.size();
    }

    public int calcVacantMembers() {
        return maxMember - crewMembers.size();
    }

    public boolean addMember(Member member) {
        if (crewMembers.size() == maxMember) {
            return false;
        }
        return crewMembers.add(member);
    }

    public boolean kickMember(Member member) {
        return crewMembers.remove(member);
    }

    public Long getCrewId() {
        return crewId;
    }

    @Override
    public String toString() {
        return "Crew{" +
                "crewId=" + crewId +
                ", crewName='" + crewName + '\'' +
                ", crewDesc='" + crewDesc + '\'' +
                ", platform=" + platform +
                ", payment=" + payment +
                ", payDate=" + payDate +
                ", maxMember=" + maxMember +
                ", account='" + account + '\'' +
                ", captain=" + (captain != null ? captain.getMemberId() : "null") +
                //", crewMembersCount=" + (crewMembers != null ? crewMembers.size() : 0) +
                '}';
    }
}
