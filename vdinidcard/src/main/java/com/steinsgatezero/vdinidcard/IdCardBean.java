package com.steinsgatezero.vdinidcard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 身份证返回参数封装类(序列化)
 */
public class IdCardBean implements Parcelable {
    private String identificationNumber;//身份证号
    private String name;//姓名
    private String validityFromDate;//身份证有效起始日期
    private String validityThruDate;//身份证有效结束日期
    private String issuingAuthorityName;//身份证签发机关
    private String gender;//身份证性别
    private String ethnicity;//身份证民族
    private String address;//身份证地址
    private String birthDate;//身份证的生日信息
    private byte[] portraits;//身份证头像
    private String snId;
    private String dnId;
    private String cardType;//港澳台居住证为J，其他为空
    private String cardSignNum;//港澳台居住证SignNum

    public IdCardBean() {
    }

    protected IdCardBean(Parcel in) {
        identificationNumber = in.readString();
        name = in.readString();
        validityFromDate = in.readString();
        validityThruDate = in.readString();
        issuingAuthorityName = in.readString();
        gender = in.readString();
        ethnicity = in.readString();
        address = in.readString();
        birthDate = in.readString();
        portraits = in.createByteArray();
        snId = in.readString();
        dnId = in.readString();
        cardType = in.readString();
        cardSignNum = in.readString();
    }

    public static final Creator<IdCardBean> CREATOR = new Creator<IdCardBean>() {
        @Override
        public IdCardBean createFromParcel(Parcel in) {
            return new IdCardBean(in);
        }

        @Override
        public IdCardBean[] newArray(int size) {
            return new IdCardBean[size];
        }
    };

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidityFromDate() {
        return validityFromDate;
    }

    public void setValidityFromDate(String validityFromDate) {
        this.validityFromDate = validityFromDate;
    }

    public String getValidityThruDate() {
        return validityThruDate;
    }

    public void setValidityThruDate(String validityThruDate) {
        this.validityThruDate = validityThruDate;
    }

    public String getIssuingAuthorityName() {
        return issuingAuthorityName;
    }

    public void setIssuingAuthorityName(String issuingAuthorityName) {
        this.issuingAuthorityName = issuingAuthorityName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getPortraits() {
        return portraits;
    }

    public void setPortraits(byte[] portraits) {
        this.portraits = portraits;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public String getDnId() {
        return dnId;
    }

    public void setDnId(String dnId) {
        this.dnId = dnId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardSignNum() {
        return cardSignNum;
    }

    public void setCardSignNum(String cardSignNum) {
        this.cardSignNum = cardSignNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(identificationNumber);
        dest.writeString(name);
        dest.writeString(validityFromDate);
        dest.writeString(validityThruDate);
        dest.writeString(issuingAuthorityName);
        dest.writeString(gender);
        dest.writeString(ethnicity);
        dest.writeString(address);
        dest.writeString(birthDate);
        dest.writeByteArray(portraits);
        dest.writeString(snId);
        dest.writeString(dnId);
        dest.writeString(cardType);
        dest.writeString(cardSignNum);
    }


    @Override
    public String toString() {
        return "IdCardBean{" +
                "identificationNumber='" + identificationNumber + '\'' +
                ", name='" + name + '\'' +
                ", validityFromDate='" + validityFromDate + '\'' +
                ", validityThruDate='" + validityThruDate + '\'' +
                ", issuingAuthorityName='" + issuingAuthorityName + '\'' +
                ", gender='" + gender + '\'' +
                ", ethnicity='" + ethnicity + '\'' +
                ", address='" + address + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", snId='" + snId + '\'' +
                ", dnId='" + dnId + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardSignNum='" + cardSignNum + '\'' +
                '}';
    }
}
