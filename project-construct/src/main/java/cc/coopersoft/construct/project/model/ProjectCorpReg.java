package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.json.JsonRawDeserializer;
import cc.coopersoft.common.json.JsonRawSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "PROJECT_JOIN_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.FIELD)
public class ProjectCorpReg extends cc.coopersoft.common.construct.project.ProjectCorpReg<JoinCorp>{

    public interface Summary {}
    public interface Details extends JoinCorp.Details{}
    public interface SummaryWithCorp extends Summary ,  Project.Title {}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private ProjectCorpReg previous;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TAGS", length = 1024)
    @JsonIgnore
    private String tags;

    @Column(name = "CORPS", length = 1024)
    @JsonDeserialize(using = JsonRawDeserializer.class)
    @JsonSerialize(using = JsonRawSerialize.class)
    @JsonView(Summary.class)
    private String corpSummary;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "corp")
    @JsonView(SummaryWithCorp.class)
    private Project project;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @Access(AccessType.PROPERTY)
    @JsonView({Summary.class,Details.class})
    @Override
    public Long getId(){return super.getId();}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @Access(AccessType.PROPERTY)
    @JsonView({Summary.class,Details.class})
    @Override
    public Date getRegTime(){return super.getRegTime();}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL, orphanRemoval = true)
    @Access(AccessType.PROPERTY)
    @JsonView(Details.class)
    @Override
    public Set<JoinCorp> getCorps(){return super.getCorps();}


}
