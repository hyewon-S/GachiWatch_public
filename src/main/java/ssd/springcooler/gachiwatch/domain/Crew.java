package ssd.springcooler.gachiwatch.domain;

import java.util.ArrayList;
import java.util.Date;

public class Crew {
    Member captain;
    ArrayList<Member> crewMembers = new ArrayList<Member>();
    String crewName;
    String crewDesc;
    //OTT ott;
    int payment;
    Date payDate;
    int maxMember;
    String account;

    public Crew(Member captain, String crewName, int maxMember) {
        this.captain = captain;
        this.crewName = crewName;
        this.maxMember = maxMember;
    }

    public Crew(Member captain, String crewName, String crewDesc, /*OTT ott,*/ int payment,
                Date payDate, int maxMember, String account) {
        super();
        this.captain = captain;
        this.crewName = crewName;
        this.crewDesc = crewDesc;
        //this.ott = ott;
        this.payment = payment;
        this.payDate = payDate;
        this.maxMember = maxMember;
        this.account = account;
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
/*
    public OTT getOtt() {
        return ott;
    }

    public void setOtt(OTT ott) {
        this.ott = ott;
    }
*/
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
