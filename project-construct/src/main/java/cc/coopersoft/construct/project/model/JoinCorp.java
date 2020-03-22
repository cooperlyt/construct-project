package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.data.ConstructJoinType;
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
@NamedEntityGraph(name = "joinCorp.full",
        attributeNodes = {@NamedAttributeNode(value = "info", subgraph = "info.reg")} ,
        subgraphs = {@NamedSubgraph(name = "info.reg", attributeNodes = @NamedAttributeNode("reg"))}
)
public class JoinCorp {

    public interface Summary{}
    public interface Details extends Summary {}
    public interface DetailsWithCorp extends Summary{}

    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @NotNull
    @JsonView(Summary.class)
    private ConstructJoinType type;

    @Column(name = "OUTSIDE_TEAM_FILE", length = 32)
    @JsonView(Details.class)
    private String outsideTeamFile;

    @Column(name = "OUT_LEVEL")
    @JsonView(Details.class)
    private Boolean outLevel;

    @Column(name = "OUT_LEVEL_FILE", length = 32)
    @JsonView(Details.class)
    private String outLevelFile;

    @Column(name = "CORP_CODE", length = 32, nullable = false)
    @JsonView(Summary.class)
    private String corpCode;

    @Column(name = "NAME", length = 128, nullable = false)
    @JsonView(Summary.class)
    private String name;

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @NotNull
    @JsonView(Summary.class)
    private GroupIdType groupIdType;

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    @JsonView(Summary.class)
    private String groupId;

    @Column(name = "LEVEL", nullable = false)
    @JsonView(Summary.class)
    private int level;

    @Column(name = "CONTACTS", length = 64)
    @JsonView(Details.class)
    private String contacts;

    @Column(name = "TEL", length = 16)
    @JsonView(Details.class)
    private String tel;

    @Column(name = "MASTER", nullable = false)
    @JsonView(Summary.class)
    private boolean master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    @JsonView(DetailsWithCorp.class)
    @JsonIgnore
    private ProjectInfo info;

}
