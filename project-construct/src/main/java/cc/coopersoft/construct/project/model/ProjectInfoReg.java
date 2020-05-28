package cc.coopersoft.construct.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PROJECT_INFO_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class ProjectInfoReg extends cc.coopersoft.common.construct.project.ProjectInfoReg{

    public interface Title {}
    public interface Summary extends Title{}
    public interface Details extends Summary {}

    @Access(AccessType.FIELD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private ProjectInfoReg previous;


    @Id
    @Access(AccessType.FIELD)
    @Column(name = "ID",nullable = false, unique = true)
    @JsonIgnore
    private Long id;


    @Access(AccessType.FIELD)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(Summary.class)
    private Date regTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROPERTY", length = 32)
    @NotNull
    @JsonView(Title.class)
    @Override
    public Property getProperty(){return super.getProperty();}


    @Column(name = "NAME", nullable = false, length = 256)
    @NotBlank
    @JsonView(Title.class)
    @Override
    public String getName(){return super.getName();};

    @Column(name = "ADDRESS", length = 512,nullable = false)
    @JsonView(Summary.class)
    @NotBlank
    @Override
    public String getAddress(){return super.getAddress();}

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 19)
    @JsonView(Title.class)
    @NotNull
    @Override
    public Type getType(){return super.getType();}

    @Column(name = "TYPE_LEVEL")
    @JsonView(Title.class)
    @Override
    public Integer getTypeLevel(){return super.getTypeLevel();}

    @Column(name = "ALL_AREA")
    @JsonView(Summary.class)
    @Override
    public BigDecimal getLandArea(){return super.getLandArea();}

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    @JsonView(Summary.class)
    @Override
    public Date getBeginDate(){return super.getBeginDate();}

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPLETED_DATE")
    @JsonView(Summary.class)
    @Override
    public Date getCompleteDate(){return super.getCompleteDate();}

    //TODO Enum
    @Column(name = "TENDER", length = 32)
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public String getTender(){return super.getTender();}

    @Column(name = "COSTS")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public BigDecimal getCosts(){return super.getCosts();}


    @Enumerated(EnumType.STRING)
    @Column(name = "MAIN_PROJECT_LEVEL", length = 8)
    @JsonView(Title.class)
    @NotNull
    @Override
    public ImportantType getImportantType(){return super.getImportantType();}

    @Column(name = "MAIN_PROJECT_FILE", length = 32)
    @JsonView(Summary.class)
    @Override
    public String getImportantFile(){return super.getImportantFile();}
}
