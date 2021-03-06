/*
 * Copyright 2018 ACINQ SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.acinq.eclair.wallet.models;

import fr.acinq.bitcoin.Transaction;
import fr.acinq.eclair.channel.CLOSED$;
import fr.acinq.eclair.channel.CLOSING$;
import fr.acinq.eclair.channel.WAIT_FOR_ACCEPT_CHANNEL$;
import fr.acinq.eclair.channel.WAIT_FOR_INIT_INTERNAL$;
import org.greenrobot.greendao.annotation.*;
import scala.Option;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(indexes = {
  @Index(value = "channelId, shortChannelId", unique = true)
})
public class LocalChannel {

  @Id
  private Long id;

  @NotNull
  @Unique
  private String channelId;
  private String shortChannelId;

  private String peerNodeId;
  private String peerAddress;

  @NotNull
  private long capacityMsat;
  @NotNull
  private long balanceMsat;

  private long refundAtBlock;

  private long channelReserveSat;

  private long minimumHtlcAmountMsat;

  /**
   * This is the time - in blocks - that we must wait before we get our funds back from the channel, in case of a local uniclose.
   */
  private int toSelfDelayBlocks;

  /**
   * This is the time
   */
  @Transient
  public int remoteToSelfDelayBlocks;

  @Transient
  public String state = "N/A";

  @Transient
  public int htlcsInFlightCount = 0;

  @Transient
  public Long feeBaseMsat;

  @Transient
  public Long feeProportionalMillionths;

  @Transient
  public Integer cltvExpiryDelta;

  public String fundingTxId;

  public String closingTxId;

  @Transient
  public List<Transaction> mainClosingTxs = new ArrayList<>();

  @Transient
  public Option<Transaction> mainDelayedClosingTx = Option.apply(null);

  @Convert(converter = ClosingTypeConverter.class, columnType = String.class)
  private ClosingType closingType;

  private String closingErrorMessage;

  private String localFeatures;

  private boolean isActive;

  @NotNull
  private Date created;

  private Date updated;

  public long getReceivableMsat() {
    return Math.max(this.getCapacityMsat() - this.getBalanceMsat() - (this.getChannelReserveSat() * 1000), 0);
  }

  public long getSendableMsat() {
    return Math.max(this.getBalanceMsat() - (this.getChannelReserveSat() * 1000), 0);
  }

  /**
   * Returns true if channels funds can be used, base on state. Funds are unusable if state is:
   * - closing/closed/shutdown
   * - in error
   * - unknown
   * - waiting for init/accept
   */
  public boolean fundsAreUsable() {
    return this.state != null && !this.state.startsWith("ERR_")
      && !WAIT_FOR_INIT_INTERNAL$.MODULE$.toString().equals(this.state)
      && !WAIT_FOR_ACCEPT_CHANNEL$.MODULE$.toString().equals(this.state)
      && !CLOSING$.MODULE$.toString().equals(this.state)
      && !CLOSED$.MODULE$.toString().equals(this.state);
  }

  public LocalChannel() {
    this.isActive = true;
    this.created = new Date();
  }

  @Generated(hash = 1662390408)
public LocalChannel(Long id, @NotNull String channelId, String shortChannelId,
        String peerNodeId, String peerAddress, long capacityMsat,
        long balanceMsat, long refundAtBlock, long channelReserveSat,
        long minimumHtlcAmountMsat, int toSelfDelayBlocks, String fundingTxId,
        String closingTxId, ClosingType closingType, String closingErrorMessage,
        String localFeatures, boolean isActive, @NotNull Date created,
        Date updated) {
    this.id = id;
    this.channelId = channelId;
    this.shortChannelId = shortChannelId;
    this.peerNodeId = peerNodeId;
    this.peerAddress = peerAddress;
    this.capacityMsat = capacityMsat;
    this.balanceMsat = balanceMsat;
    this.refundAtBlock = refundAtBlock;
    this.channelReserveSat = channelReserveSat;
    this.minimumHtlcAmountMsat = minimumHtlcAmountMsat;
    this.toSelfDelayBlocks = toSelfDelayBlocks;
    this.fundingTxId = fundingTxId;
    this.closingTxId = closingTxId;
    this.closingType = closingType;
    this.closingErrorMessage = closingErrorMessage;
    this.localFeatures = localFeatures;
    this.isActive = isActive;
    this.created = created;
    this.updated = updated;
}

public Long getId() {
      return this.id;
  }

  public void setId(Long id) {
      this.id = id;
  }

  public String getChannelId() {
      return this.channelId;
  }

  public void setChannelId(String channelId) {
      this.channelId = channelId;
  }

  public String getShortChannelId() {
      return this.shortChannelId;
  }

  public void setShortChannelId(String shortChannelId) {
      this.shortChannelId = shortChannelId;
  }

  public String getPeerNodeId() {
      return this.peerNodeId;
  }

  public void setPeerNodeId(String peerNodeId) {
      this.peerNodeId = peerNodeId;
  }

  public String getPeerAddress() {
      return this.peerAddress;
  }

  public void setPeerAddress(String peerAddress) {
      this.peerAddress = peerAddress;
  }

  public long getCapacityMsat() {
      return this.capacityMsat;
  }

  public void setCapacityMsat(long capacityMsat) {
      this.capacityMsat = capacityMsat;
  }

  public long getBalanceMsat() {
      return this.balanceMsat;
  }

  public void setBalanceMsat(long balanceMsat) {
      this.balanceMsat = balanceMsat;
  }

  public long getChannelReserveSat() {
      return this.channelReserveSat;
  }

  public void setChannelReserveSat(long channelReserveSat) {
      this.channelReserveSat = channelReserveSat;
  }

  public long getMinimumHtlcAmountMsat() {
      return this.minimumHtlcAmountMsat;
  }

  public void setMinimumHtlcAmountMsat(long minimumHtlcAmountMsat) {
      this.minimumHtlcAmountMsat = minimumHtlcAmountMsat;
  }

  public int getToSelfDelayBlocks() {
      return this.toSelfDelayBlocks;
  }

  public void setToSelfDelayBlocks(int toSelfDelayBlocks) {
      this.toSelfDelayBlocks = toSelfDelayBlocks;
  }

  public String getFundingTxId() {
      return this.fundingTxId;
  }

  public void setFundingTxId(String fundingTxId) {
      this.fundingTxId = fundingTxId;
  }

  public String getClosingTxId() {
      return this.closingTxId;
  }

  public void setClosingTxId(String closingTxId) {
      this.closingTxId = closingTxId;
  }

  public ClosingType getClosingType() {
      return this.closingType;
  }

  public void setClosingType(ClosingType closingType) {
      this.closingType = closingType;
  }

  public String getClosingErrorMessage() {
      return this.closingErrorMessage;
  }

  public void setClosingErrorMessage(String closingErrorMessage) {
      this.closingErrorMessage = closingErrorMessage;
  }

  public String getLocalFeatures() {
      return this.localFeatures;
  }

  public void setLocalFeatures(String localFeatures) {
      this.localFeatures = localFeatures;
  }

  public boolean getIsActive() {
      return this.isActive;
  }

  public void setIsActive(boolean isActive) {
      this.isActive = isActive;
  }

  public Date getCreated() {
      return this.created;
  }

  public void setCreated(Date created) {
      this.created = created;
  }

  public Date getUpdated() {
      return this.updated;
  }

  public void setUpdated(Date updated) {
      this.updated = updated;
  }

public long getRefundAtBlock() {
    return this.refundAtBlock;
}

public void setRefundAtBlock(long refundAtBlock) {
    this.refundAtBlock = refundAtBlock;
}

}
