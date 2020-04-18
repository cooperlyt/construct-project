package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.data.GroupIdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PROJECT_JOIN_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
//@NamedEntityGraph(name = "joinCorp.full",
//        attributeNodes = {@NamedAttributeNode(value = "reg", subgraph = "reg.info")} ,
//        subgraphs = {@NamedSubgraph(name = "reg.info", attributeNodes = @NamedAttributeNode("info"))}
//)
public class JoinCorp extends cc.coopersoft.common.construct.project.JoinCorp<JoinCorpInfo>{


    public interface Summary{}
    public interface Details extends Summary{}

    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private ProjectCorpReg reg;

    @Enumerated(EnumType.STRING)
    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @NotNull
    @JsonView(Details.class)
    @Override
    public CorpProperty getProperty(){return super.getProperty();}

    @Column(name = "OUTSIDE_TEAM_FILE", length = 32)
    @JsonView(Details.class)
    @Override
    public String getOutsideTeamFile(){return super.getOutsideTeamFile();}

    @Column(name = "OUT_LEVEL")
    @JsonView(Details.class)
    @NotNull
    @Override
    public Boolean getOutLevel(){return super.getOutLevel();}

    @Column(name = "OUT_LEVEL_FILE", length = 32)
    @JsonView(Details.class)
    @Override
    public String getOutLevelFile(){return super.getOutLevelFile();}

    @Column(name = "CORP_CODE", nullable = false)
    @JsonView(Details.class)
    @Override
    public long getCode(){return super.getCode();}



    @Column(name = "CONTACTS", length = 64)
    @JsonView(Details.class)
    @Override
    public String getContacts(){return super.getContacts();}

    @Column(name = "TEL", length = 16)
    @JsonView(Details.class)
    @Override
    public String getTel(){return super.getTel();}

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, optional = false)
    @PrimaryKeyJoinColumn
    @JsonView(Summary.class)
    @Override
    public JoinCorpInfo getInfo(){return super.getInfo();}



}
