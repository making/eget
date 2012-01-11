package am.ik.eget.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "url")
    private String url;
    
    @Column(name = "cid")
    private String cid;

    @Column(name = "title")
    private String title;

    @ManyToMany(targetEntity = Actress.class)
    @JoinTable(name = "movie_actress", joinColumns = @JoinColumn(name = "movie_url", referencedColumnName = "url"), inverseJoinColumns = @JoinColumn(name = "actress_id", referencedColumnName = "id"))
    private List<Actress> actresses;

    @Column(name = "part")
    private int part;

    @Column(name = "saved")
    private boolean saved = false;

    @Column(name = "drm")
    private boolean drm = true;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Actress> getActresses() {
        return actresses;
    }

    public void setActresses(List<Actress> actresses) {
        this.actresses = actresses;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isDrm() {
        return drm;
    }

    public void setDrm(boolean drm) {
        this.drm = drm;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}