package me.yh.community.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@MappedSuperclass
public abstract class Address {
    @Column(length = 30)
    private String zoneCode;         //우편번호
    @Column(length = 300)
    private String address;          //기본주소
    @Column(length = 300)
    private String extraAddress;     //조합형 주소 변수
    @Column(length = 500)
    private String detailAddress;    //상세주소

    public void changeFullAddress(String zoneCode, String address, String extraAddress, String detailAddress) {
        this.zoneCode = zoneCode;
        this.address = address;
        this.extraAddress = extraAddress;
        this.detailAddress = detailAddress;
    }

    public void changeDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}




