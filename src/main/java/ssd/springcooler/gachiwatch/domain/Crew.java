package ssd.springcooler.gachiwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@Entity
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int crewId;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    Member captain;

    @ManyToMany(mappedBy = "joinedCrews")
    ArrayList<Member> crewMembers = new ArrayList<Member>();
    String crewName;
    String crewDesc;
    @Enumerated(EnumType.STRING)
    Platform ott;
    int payment;
    Date payDate;
    int maxMember;
    String account;

    public Crew(Member captain, String crewName, int maxMember) {
        this.captain = captain;
        this.crewName = crewName;
        this.maxMember = maxMember;
    }

    public Crew(Member captain, String crewName, String crewDesc, Platform ott, int payment,
                Date payDate, int maxMember, String account) {
        super();
        this.captain = captain;
        this.crewName = crewName;
        this.crewDesc = crewDesc;
        this.ott = ott;
        this.payment = payment;
        this.payDate = payDate;
        this.maxMember = maxMember;
        this.account = account;
    }

    public Crew() {

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

    public Member getCaptain() {
        return captain;
    }

    public void setCaptain(Member captain) {
        this.captain = captain;
    }

    public ArrayList<Member> getCrewMembers() {
        return crewMembers;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getCrewDesc() {
        return crewDesc;
    }

    public void setCrewDesc(String crewDesc) {
        this.crewDesc = crewDesc;
    }

    public Platform getOtt() {
        return ott;
    }

    public void setOtt(Platform ott) {
        this.ott = ott;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
