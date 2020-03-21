package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.data.ConstructJoinType;
import cc.coopersoft.common.data.GroupIdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@NamedEntityGraph(name = "joinCorp.full", attributeNodes = {@NamedAttributeNode(value = "info")})
public class JoinCorp {

    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @NotNull
    private ConstructJoinType type;

    @Column(name = "OUTSIDE_TEAM_FILE", length = 32)
    private String outsideTeamFile;

    @Column(name = "OUT_LEVEL")
    private Boolean outLevel;

    @Column(name = "OUT_LEVEL_FILE", length = 32)
    private String outLevelFile;

    @Column(name = "CORP_CODE", length = 32, nullable = false)
    private String corpCode;

    @Column(name = "NAME", length = 128, nullable = false)
    private String name;

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupIdType groupIdType;

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String groupId;

    @Column(name = "LEVEL", nullable = false)
    private int level;

    @Column(name = "CONTACTS", length = 64)
    private String contacts;

    @Column(name = "TEL", length = 16)
    private String tel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    private ProjectInfo info;

}
