package com.lostandfound.model;

public class FoundReport extends ItemReport {

    public FoundReport() { super(); }

    @Override
    public ReportType getReportType()
    {
        return ReportType.FOUND;
    }

    @Override
    public String getDateLabel()
    {
        return "Date found";
    }
}