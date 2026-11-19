package com.lostandfound.model;

import java.time.LocalDateTime;

public class Claim {
    private int claimId;
    private int foundReportId;
    private int claimantId;
    private String ownershipDetails;
    private ClaimStatus status = ClaimStatus.PENDING;
    private Integer reviewerId;
    private String decisionReason;
    private LocalDateTime decisionDate;
    private LocalDateTime createdAt;

    public Claim() {}

    public Claim(int foundReportId, int claimantId, String ownershipDetails) {
        this.foundReportId = foundReportId;
        this.claimantId = claimantId;
        this.ownershipDetails = ownershipDetails;
    }

    public void validate()
    {
        if (foundReportId <= 0)
        {
            throw new IllegalArgumentException("A found report is required");
        }
        if (claimantId <= 0)
        {
            throw new IllegalArgumentException("Claimant is required");
        }
        if (ownershipDetails == null || ownershipDetails.trim().length() < 10)
        {
            throw new IllegalArgumentException("Please explain ownership in at least 10 characters");
        }
    }

    public int getClaimId()
    {
        return claimId;
    }

    public void setClaimId(int claimId)
    {
        this.claimId = claimId;
    }

    public int getFoundReportId()
    {
        return foundReportId;
    }

    public void setFoundReportId(int foundReportId)
    {
        this.foundReportId = foundReportId;
    }

    public int getClaimantId()
    {
        return claimantId;
    }

    public void setClaimantId(int claimantId)
    {
        this.claimantId = claimantId;
    }

    public String getOwnershipDetails()
    {
        return ownershipDetails;
    }

    public void setOwnershipDetails(String ownershipDetails)
    {
        this.ownershipDetails = ownershipDetails;
    }

    public ClaimStatus getStatus()
    {
        return status;
    }

    public void setStatus(ClaimStatus status)
    {
        this.status = status;
    }

    public Integer getReviewerId()
    {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId)
    {
        this.reviewerId = reviewerId;
    }

    public String getDecisionReason()
    {
        return decisionReason;
    }

    public void setDecisionReason(String decisionReason)
    {
        this.decisionReason = decisionReason;
    }

    public LocalDateTime getDecisionDate()
    {
        return decisionDate;
    }

    public void setDecisionDate(LocalDateTime decisionDate)
    {
        this.decisionDate = decisionDate;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}