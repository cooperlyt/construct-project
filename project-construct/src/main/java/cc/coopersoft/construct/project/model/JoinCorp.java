package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.construct.corp.CorpProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PROJECT_JOIN_CORP",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CORP_PROPERTY_REG",columnNames = {"CORP_CODE", "CORP_TYPE", "REG"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
@NamedEntityGraph(name = "joinCorp.details",
        attributeNodes = {@NamedAttributeNode(value = "reg", subgraph = "reg.project")} ,
        subgraphs = {

            @NamedSubgraph(name = "reg.project", attributeNodes = @NamedAttributeNode(value = "project", subgraph="project.info")),
                @NamedSubgraph(name = "project.info", attributeNodes = @NamedAttributeNode(value = "info", subgraph = "project.info.info"))  ,
                    @NamedSubgraph(name = "project.info.info", attributeNodes = @NamedAttributeNode(value = "info"))
        }
)
public class JoinCorp extends cc.coopersoft.common.construct.project.JoinCorp<JoinCorpInfo>{

    //从 project 取建设单位时使用
    public interface Summary extends JoinCorpInfo.Summary {}

    public interface Details extends Summary, JoinCorpInfo.Details {}

    //取 单位 的项目列表时使用
    public interface SummaryWithCorp extends JoinCorpInfo.Summary, ProjectCorpReg.SummaryWithCorp {}

    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @NotNull
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public CorpProperty getProperty(){return super.getProperty();}

    @Column(name = "OUTSIDE_TEAM_FILE", length = 32)
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public String getOutsideTeamFile(){return super.getOutsideTeamFile();}

    @Column(name = "OUT_LEVEL")
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public Boolean getOutLevel(){return super.getOutLevel();}

    @Column(name = "OUT_LEVEL_FILE", length = 32)
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public String getOutLevelFile(){return super.getOutLevelFile();}

    @Column(name = "CORP_CODE", nullable = false)
    @JsonView({Summary.class,SummaryWithCorp.class})
    @Override
    public long getCode(){return super.getCode();}

    @Column(name = "CONTACTS", length = 64)
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public String getContacts(){return super.getContacts();}

    @Column(name = "TEL", length = 16)
    @JsonView({Details.class, SummaryWithCorp.class})
    @Override
    public String getTel(){return super.getTel();}

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, optional = false)
    @PrimaryKeyJoinColumn
    @JsonView({Summary.class,SummaryWithCorp.class})
    @Override
    public JoinCorpInfo getInfo(){return super.getInfo();}

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG", nullable = false)
    @Access(AccessType.FIELD)
    @JsonView(SummaryWithCorp.class)
    private ProjectCorpReg reg;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JoinCorp joinCorp = (JoinCorp) o;

        if (id == null || joinCorp.id == null) return false;
        return id.equals(joinCorp.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
