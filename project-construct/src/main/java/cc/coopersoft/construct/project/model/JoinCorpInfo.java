package cc.coopersoft.construct.project.model;


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
@Table(name = "PROJECT_JOIN_CORP_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class JoinCorpInfo extends cc.coopersoft.common.construct.project.JoinCorpInfo{

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private JoinCorp joinCorp;

    @Column(name = "NAME", length = 128, nullable = false)
    @JsonView(JoinCorp.Details.class)
    @Override
    public String getName(){return super.getName();}

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @JsonView(JoinCorp.Details.class)
    @Override
    public GroupIdType getGroupIdType(){return super.getGroupIdType();}

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @JsonView(JoinCorp.Details.class)
    @Override
    public String getGroupId(){return super.getGroupId();}

    @Column(name = "LEVEL", nullable = false)
    @JsonView(JoinCorp.Details.class)
    @Override
    public int getLevel(){return super.getLevel();}


}
