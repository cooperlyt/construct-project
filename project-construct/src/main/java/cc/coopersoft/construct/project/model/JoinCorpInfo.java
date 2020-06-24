package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.PersonIdType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "PROJECT_JOIN_CORP_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class JoinCorpInfo extends cc.coopersoft.common.construct.project.JoinCorpInfo{

    public interface Summary {}
    public interface Details extends Summary {}

    //MapsId bug see https://hibernate.atlassian.net/browse/HHH-12436?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
    @Id
    @Column(name = "JOIN_ID", nullable = false, unique = true)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;


    @Access(AccessType.FIELD)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOIN_ID",columnDefinition = "JOIN_ID")
    @MapsId
    @JsonBackReference
    private JoinCorp corp;

    @Column(name = "NAME", length = 128, nullable = false)
    @Override
    @JsonView(Summary.class)
    public String getName(){return super.getName();}

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @JsonView(Details.class)
    @Override
    public GroupIdType getGroupIdType(){return super.getGroupIdType();}

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @JsonView(Details.class)
    @Override
    public String getGroupId(){return super.getGroupId();}

    @Column(name = "LEVEL", nullable = false)
    @JsonView(Summary.class)
    @Override
    public int getLevel(){return super.getLevel();}

    @Column(name ="OWNER_NAME", length = 32, nullable = false)
    @JsonView(Details.class)
    @Override
    public String getOwnerName(){return super.getOwnerName();}

    @Enumerated(EnumType.STRING)
    @Column(name ="OWNER_ID_TYPE", length = 16, nullable = false)
    @JsonView(Details.class)
    @Override
    public PersonIdType getOwnerIdType(){return super.getOwnerIdType();}

    @Column(name ="OWNER_ID_NUMBER", length = 32, nullable = false)
    @JsonView(Details.class)
    @Override
    public String getOwnerId(){return super.getOwnerId();}

}
