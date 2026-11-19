package com.lostandfound.model;

public class LostReport extends ItemReport {

    public LostReport() { super(); }

    @Override
    public ReportType getReportType()
    {
        return ReportType.LOST;
    }

    @Override
    public String getDateLabel()
    {
        return "Date lost";
    }
}