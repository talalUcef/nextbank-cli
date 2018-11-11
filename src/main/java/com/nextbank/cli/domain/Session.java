package com.nextbank.cli.domain;

import java.util.Date;
import java.util.Objects;

public class Session {

    private final Long id;
    private final Date startDate;
    private final String username;
    private Date endDate;

    public Session(Long id, Date startDate, String username) {
        this.id = id;
        this.startDate = startDate;
        this.username = username;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Session{");
        sb.append("id=").append(id);
        sb.append(", startDate=").append(startDate);
        sb.append(", username='").append(username).append('\'');
        sb.append(", endDate=").append(endDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getUsername() {
        return username;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Long getId() {
        return id;
    }
}
