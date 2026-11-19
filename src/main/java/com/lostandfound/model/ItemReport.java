package com.lostandfound.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ItemReport {
    private int reportId;
    private int userId;
    private int categoryId;
    private String categoryName;
    private String title;
    private String description;
    private String color;
    private String brand;
    private String distinguishingFeatures;
    private LocalDate eventDate;
    private String location;
    private String imagePath;
    private ReportStatus status = ReportStatus.OPEN;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected ItemReport() {}

    public abstract ReportType getReportType();

    public abstract String getDateLabel();

    public boolean isOpen()
    {
        return status == ReportStatus.OPEN;
    }

    public void validate()
    {
        if (title == null || title.isBlank())
        {
            throw new IllegalArgumentException("Title is required");
        }
        if (categoryId <= 0)
        {
            throw new IllegalArgumentException("Category is required");
        }
        if (eventDate == null)
        {
            throw new IllegalArgumentException("Date is required");
        }
        if (eventDate.isAfter(LocalDate.now()))
        {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
    }

    public int getReportId()
    {
        return reportId;
    }

    public void setReportId(int reportId)
    {
        this.reportId = reportId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getDistinguishingFeatures()
    {
        return distinguishingFeatures;
    }

    public void setDistinguishingFeatures(String distinguishingFeatures)
    {
        this.distinguishingFeatures = distinguishingFeatures;
    }

    public LocalDate getEventDate()
    {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate)
    {
        this.eventDate = eventDate;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public ReportStatus getStatus()
    {
        return status;
    }

    public void setStatus(ReportStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}