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
@Table(name = "PROJECT_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class ProjectInfo extends cc.coopersoft.common.construct.project.ProjectInfo implements java.io.Serializable{


    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 256)
    @NotBlank
    @JsonView(ProjectInfoReg.Title.class)
    @Override
    public String getName(){return super.getName();};

    @Column(name = "ADDRESS", length = 512)
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public String getAddress(){return super.getAddress();}

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 19)
    @JsonView(ProjectInfoReg.Title.class)
    @NotNull
    @Override
    public Type getType(){return super.getType();}

    @Column(name = "TYPE_CLASS")
    @JsonView(ProjectInfoReg.Title.class)
    @Override
    public Integer getTypeLevel(){return super.getTypeLevel();}

    @Column(name = "FLOOR_TYPE", length = 6)
    @JsonView(ProjectInfoReg.Title.class)
    @Override
    public FloorType getFloorType(){return super.getFloorType();}

    @Enumerated(EnumType.STRING)
    @Column(name = "PROPERTY", length = 32)
    @NotNull
    @JsonView(ProjectInfoReg.Title.class)
    @Override
    public Property getProperty(){return super.getProperty();}

    @Column(name = "CONTRACT_AREA")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public BigDecimal getArea(){return super.getArea();}

    @Column(name = "ALL_AREA")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public BigDecimal getLandArea(){return super.getLandArea();}

    @Column(name = "GROUND_FLOOR_COUNT")
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public Integer getGroundCount(){return super.getGroundCount();}

    @Column(name = "UNDER_FLOOR_COUNT")
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public Integer getUnderCount(){return super.getUnderCount();}

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public Date getBeginDate(){return super.getBeginDate();}

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPLETED_DATE")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public Date getCompleteDate(){return super.getCompleteDate();}

    //TODO Enum
    @Column(name = "TENDER", length = 32)
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public String getTender(){return super.getTender();}

    @Enumerated(EnumType.STRING)
    @Column(name = "STRUCTURE", length = 16)
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public Struct getStructure(){return super.getStructure();}

    @Column(name = "COSTS")
    @JsonView(ProjectInfoReg.Summary.class)
    @Override
    public BigDecimal getCosts(){return super.getCosts();}


    @Enumerated(EnumType.STRING)
    @Column(name = "MAIN_PROJECT_LEVEL", length = 8)
    @JsonView(ProjectInfoReg.Title.class)
    @NotNull
    @Override
    public ImportantType getImportantType(){return super.getImportantType();}

    @Column(name = "MAIN_PROJECT_FILE", length = 32)
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public String getImportantFile(){return super.getImportantFile();}

    @Column(name = "MEMO", length = 512)
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public String getMemo(){return super.getMemo();}

    @Column(name = "HEIGHT")
    @JsonView(ProjectInfoReg.Details.class)
    @Override
    public BigDecimal getHeight(){return super.getHeight();}




}
