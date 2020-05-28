package cc.coopersoft.construct.project.model;

import cc.coopersoft.common.data.OperationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "CONSTRUCT_PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "project.summary",
        attributeNodes = {
            @NamedAttributeNode(value = "info"),
            @NamedAttributeNode(value = "corp"),
            @NamedAttributeNode(value ="developer", subgraph = "corp.info")} ,
        subgraphs = {
            @NamedSubgraph(name = "corp.info", attributeNodes = @NamedAttributeNode("info"))
        }
)
public class Project extends cc.coopersoft.common.construct.project.Project<ProjectInfoReg,JoinCorp>{


    public interface Title extends ProjectInfoReg.Title{}
    public interface Summary extends Title, ProjectInfoReg.Summary, ProjectCorpReg.Summary , JoinCorp.Summary{}
    public interface Details extends Title, ProjectInfoReg.Details, ProjectCorpReg.Details , BuildRegInfo.View {}


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Date regTime;

    @Id
    @Column(name = "PROJECT_CODE", nullable = false, unique = true)
    @JsonView(Title.class)
    @Override
    public long getCode(){return super.getCode();}

    @Column(name = "ENABLE", nullable = false)
    @JsonView(Title.class)
    @Override
    public boolean isEnable(){return super.isEnable();}

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEVELOPER", nullable = false)
    @JsonView(Summary.class)
    @Override
    public JoinCorp getDeveloper(){return super.getDeveloper();}

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INFO", nullable = false)
    @JsonView(Title.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Override
    public ProjectInfoReg getInfo(){return super.getInfo();}

    @Access(AccessType.FIELD)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP", nullable = false)
    private ProjectCorpReg corp;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Details.class)
    @Override
    public List<JoinCorp> getCorps(){
       return this.getCorp().getCorps();
    }

    @Access(AccessType.FIELD)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUILD", nullable = false)
    private BuildReg build;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Details.class)
    @Override
    public List<BuildRegInfo> getBuilds(){
        return this.getBuild().getBuilds().stream().filter(build -> !OperationType.DELETE.equals(build.getOperation())).collect(Collectors.toList());
    }

    @Access(AccessType.FIELD)
    @Version
    @Column(name = "VERSION")
    @JsonIgnore
    private Integer version;
}
