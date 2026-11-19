package com.lostandfound.model;

import java.time.LocalDateTime;

public class Activity {
    private int activityId;
    private int userId;
    private String action;
    private String details;
    private Integer reportId;
    private Integer claimId;
    private LocalDateTime createdAt;

    public Activity() {}

    public Activity(int userId, String action, String details, Integer reportId, Integer claimId) {
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.reportId = reportId;
        this.claimId = claimId;
    }

    public int getActivityId()
    {
        return activityId;
    }

    public void setActivityId(int activityId)
    {
        this.activityId = activityId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    public Integer getReportId()
    {
        return reportId;
    }

    public void setReportId(Integer reportId)
    {
        this.reportId = reportId;
    }

    public Integer getClaimId()
    {
        return claimId;
    }

    public void setClaimId(Integer claimId)
    {
        this.claimId = claimId;
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