package cc.coopersoft.construct.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @Access(AccessType.FIELD)
    @JsonIgnore
    private ProjectInfoReg previous;

    @Column(name = "REDISTERED", nullable = false)
    @Access(AccessType.FIELD)
    @JsonIgnore
    private boolean registered;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "info")
    @JoinColumn(name = "ID", nullable = false)
    @JsonIgnore
    private ProjectReg reg;

    @Id
    @Column(name = "ID",nullable = false, unique = true)
    @Override
    public Long getId(){return super.getId();}

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "INFO", nullable = false)
    @Override
    public ProjectInfo getInfo(){return super.getInfo();}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @Override
    public Date getRegTime(){return super.getRegTime();}



}
