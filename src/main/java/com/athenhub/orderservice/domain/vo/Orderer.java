package com.athenhub.orderservice.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Orderer {

  @Column(nullable = false, name = "orderer_id")
  private UUID id;

  @Column(name = "orderer_name")
  private String name;

  @Column(name = "orderer_slackId")
  private String slackId;

  private Orderer(UUID id, String name, String slackId) {
    this.id = Objects.requireNonNull(id);
    this.name = Objects.requireNonNull(name);
    this.slackId = slackId;
  }

  public static Orderer of(UUID id, String name, String slackId) {
    return new Orderer(id, name, slackId);
  }
}
