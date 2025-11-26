package com.athenhub.orderservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 배송지 정보를 나타내는 값 객체이다.
 *
 * <p>주문에 대한 기본 배송지와 상세 주소를 함께 보관한다. Order Aggregate 내부에서만 사용되는 불변(immutable)한 VO이다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingAddress {

  /** 기본 배송지 주소. */
  @Column(name = "shiping_address")
  private String address;

  /** 상세 배송지 주소(예: 동, 호수, 층수 등). */
  @Column(name = "shiping_detail_address")
  private String detailAddress;

  /**
   * ShippingAddress 생성자이다.
   *
   * <p>외부에서 직접 호출하지 않으며 {@link #of(String, String)}를 통해 생성된다.
   *
   * @param address 기본 주소
   * @param detailAddress 상세 주소
   */
  private ShippingAddress(String address, String detailAddress) {
    this.address = address;
    this.detailAddress = detailAddress;
  }

  /**
   * ShippingAddress를 생성한다.
   *
   * @param address 기본 주소
   * @param detailAddress 상세 주소
   * @return 생성된 ShippingAddress
   */
  public static ShippingAddress of(String address, String detailAddress) {
    return new ShippingAddress(address, detailAddress);
  }
}
