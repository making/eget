package am.ik.eget.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "actress")
public class Actress implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Actress NO_ACTRESS;
    static {
        NO_ACTRESS = new Actress();
        NO_ACTRESS.setId(0L);
        NO_ACTRESS.setName("----");
        NO_ACTRESS.setMovies(Collections.<Movie> emptyList());
    }

    @Id
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToMany(targetEntity = Movie.class, mappedBy = "actresses")
    private List<Movie> movies;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name)
                .toString();
    }
}