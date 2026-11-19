package com.lostandfound.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class MatchResult implements Comparable<MatchResult> {
    private ItemReport lostReport;
    private ItemReport foundReport;
    private double totalScore;
    private Map<String, Double> breakdown = new LinkedHashMap<>();

    public MatchResult(ItemReport lostReport, ItemReport foundReport) {
        this.lostReport = lostReport;
        this.foundReport = foundReport;
    }

    public void addCriterion(String name, double score)
    {
        breakdown.put(name, score);
    }

    public ItemReport getLostReport()
    {
        return lostReport;
    }

    public ItemReport getFoundReport()
    {
        return foundReport;
    }

    public double getTotalScore()
    {
        return totalScore;
    }

    public void setTotalScore(double totalScore)
    {
        this.totalScore = totalScore;
    }

    public Map<String, Double> getBreakdown()
    {
        return breakdown;
    }

    @Override
    public int compareTo(MatchResult other)
    {
        return Double.compare(other.totalScore, this.totalScore); // higher score first
    }
}