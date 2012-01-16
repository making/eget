package am.ik.eget.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "page")
public class Page {
    @Id
    @Column(name = "url")
    private String url;
    
    @Column(name = "page")
    private int page = 0;
    
    @Column(name="type")
    private String type;
    
    //@Column(name = "channel")
    @Transient
    private String channel;
    
    @Column(name="extracted")
    private boolean extracted = false;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExtracted() {
        return extracted;
    }

    public void setExtracted(boolean extracted) {
        this.extracted = extracted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Page [url=" + url + ", page=" + page + ", type=" + type
                + ", extracted=" + extracted + ", createdAt=" + createdAt + "]";
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
}
