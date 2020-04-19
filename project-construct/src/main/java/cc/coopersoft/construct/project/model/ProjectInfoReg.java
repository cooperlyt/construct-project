package cc.coopersoft.construct.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PROJECT_INFO_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class ProjectInfoReg extends cc.coopersoft.common.construct.project.ProjectInfoReg<ProjectInfo>{

    public interface Title {}
    public interface Summary extends Title{}
    public interface Details extends Summary {}

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PREVIOUS")
    @Access(AccessType.FIELD)
    @JsonIgnore
    private ProjectInfoReg previous;


    @MapsId
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "info")
    @JoinColumn(name = "ID", nullable = false)
    @JsonIgnore
    private ProjectReg reg;

    @Id
    @Column(name = "ID",nullable = false, unique = true)
    @Override
    @JsonView(Title.class)
    public Long getId(){return super.getId();}

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "INFO", nullable = false)
    @JsonView(Title.class)
    @Override
    public ProjectInfo getInfo(){return super.getInfo();}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(Summary.class)
    @Override
    public Date getRegTime(){return super.getRegTime();}



}
