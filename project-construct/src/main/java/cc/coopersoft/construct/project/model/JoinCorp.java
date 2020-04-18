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
import javax.validation.constraints.Size;

@Entity
@Table(name = "PROJECT_JOIN_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
//@NamedEntityGraph(name = "joinCorp.full",
//        attributeNodes = {@NamedAttributeNode(value = "reg", subgraph = "reg.info")} ,
//        subgraphs = {@NamedSubgraph(name = "reg.info", attributeNodes = @NamedAttributeNode("info"))}
//)
public class JoinCorp extends cc.coopersoft.common.construct.project.JoinCorp{


    public interface Details{}

    public enum Role {
        MASTER,
        ACCOMPANY,
        MANAGER
    }

    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @NotNull
    @JsonView(Details.class)
    private CorpProperty property;

    @Column(name = "OUTSIDE_TEAM_FILE", length = 32)
    @JsonView(Details.class)
    private String outsideTeamFile;

    @Column(name = "OUT_LEVEL")
    @JsonView(Details.class)
    @NotNull
    private Boolean outLevel;

    @Column(name = "OUT_LEVEL_FILE", length = 32)
    @JsonView(Details.class)
    private String outLevelFile;

    @Column(name = "CORP_CODE", nullable = false)
    @JsonView(Details.class)
    private long corpCode;

    @Column(name = "NAME", length = 128, nullable = false)
    @JsonView(Details.class)
    private String name;

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @JsonView(Details.class)
    private GroupIdType groupIdType;

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @JsonView(Details.class)
    private String groupId;

    @Column(name = "LEVEL", nullable = false)
    @JsonView(Details.class)
    private int level;

    @Column(name = "CONTACTS", length = 64)
    @JsonView(Details.class)
    private String contacts;

    @Column(name = "TEL", length = 16)
    @JsonView(Details.class)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false , length = 16)
    @JsonView(Details.class)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG", nullable = false)
    @JsonIgnore
    private ProjectReg reg;

}
