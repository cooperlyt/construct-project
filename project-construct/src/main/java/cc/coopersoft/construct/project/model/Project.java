package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONSTRUCT_PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "project.summary",
        attributeNodes = {@NamedAttributeNode(value = "reg", subgraph = "reg.info")} ,
        subgraphs = {@NamedSubgraph(name = "reg.info", attributeNodes = @NamedAttributeNode("info"))}
)
public class Project extends cc.coopersoft.common.construct.project.Project<ProjectInfoReg,JoinCorp,ProjectCorpReg>{

    public interface Summary extends ProjectReg.Summary {}
    public interface Details extends Summary, ProjectReg.Details {}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Date dataTime;

    @Id
    @Column(name = "PROJECT_CODE", nullable = false, unique = true)
    @JsonView(Summary.class)
    @Override
    public Long getCode(){return super.getCode();}

    @Column(name = "ENABLE", nullable = false)
    @JsonView(Summary.class)
    @Override
    public boolean isEnable(){return super.isEnable();}


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INFO", nullable = false)
    @Override
    public ProjectInfoReg getInfo(){return super.getInfo();}

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP", nullable = false)
    @Override
    public ProjectCorpReg getCorp(){return super.getCorp();}

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEVELOPER", nullable = false)
    @Override
    public JoinCorp getDeveloper(){return super.getDeveloper();}


}
