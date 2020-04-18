package cc.coopersoft.construct.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "PROJECT_JOIN_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class ProjectCorpReg extends cc.coopersoft.common.construct.project.ProjectCorpReg<JoinCorp>{

    @Column(name = "OWNER", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private boolean owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    @Access(AccessType.FIELD)
    private ProjectCorpReg previous;

    @Column(name = "REDISTERED", nullable = false)
    @Access(AccessType.FIELD)
    @JsonIgnore
    private boolean registered;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @Override
    public Long getId(){return super.getId();}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @Override
    public Date getRegTime(){return super.getRegTime();}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL)
    @Override
    public Set<JoinCorp> getCorps(){return super.getCorps();}


    @MapsId
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "corp")
    @JoinColumn(name = "ID", nullable = false)
    @JsonIgnore
    private ProjectReg reg;

}
