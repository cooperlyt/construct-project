package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PROJECT_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class ProjectInfo extends cc.coopersoft.common.construct.project.ProjectInfo implements java.io.Serializable{

    public interface Summary {}
    public interface Details extends Summary {}


    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 256)
    @NotBlank
    @JsonView(Summary.class)
    @Override
    public String getName(){return super.getName();};

    @Column(name = "ADDRESS", length = 512)
    @JsonView(Summary.class)
    @Override
    public String getAddress(){return getAddress();}

    //TODO Enum maybe not null
    @Column(name = "TYPE", length = 32)
    @JsonView(Summary.class)
    @Override
    public String getType(){return getType();}

    //TODO Enum maybe not null
    @Column(name = "PROPERTY", length = 32)
    @JsonView(Summary.class)
    @Override
    public String getProperty(){return super.getProperty();}

    @Column(name = "CONTRACT_AREA")
    @JsonView(Summary.class)
    @Override
    public BigDecimal getArea(){return super.getArea();}

    @Column(name = "ALL_AREA")
    @JsonView(Summary.class)
    @Override
    public BigDecimal getLandArea(){return super.getLandArea();}

    @Column(name = "GROUND_FLOOR_COUNT")
    @JsonView(Summary.class)
    @Override
    public Integer getGroundCount(){return super.getGroundCount();}

    @Column(name = "UNDER_FLOOR_COUNT")
    @JsonView(Summary.class)
    @Override
    public Integer getUnderCount(){return super.getUnderCount();}

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
    @JsonView(Details.class)
    @Override
    public String getTender(){return super.getTender();}

    //TODO Enum
    @Column(name = "STRUCTURE", length = 32)
    @JsonView(Summary.class)
    @Override
    public String getStructure(){return super.getStructure();}

    @Column(name = "COSTS")
    @JsonView(Summary.class)
    @Override
    public BigDecimal getCosts(){return super.getCosts();}

    @Column(name = "MAIN_PROJECT")
    @JsonView(Summary.class)
    @Override
    public Boolean getImportant(){return super.getImportant();}

    //TODO Enum or int
    @Column(name = "MAIN_PROJECT_LEVEL", length = 16)
    @JsonView(Summary.class)
    @Override
    public String getImportantLevel(){return super.getImportantLevel();}

    @Column(name = "MAIN_PROJECT_FILE", length = 32)
    @JsonView(Details.class)
    @Override
    public String getImportantFile(){return super.getImportantFile();}

    @Column(name = "MEMO", length = 512)
    @JsonView(Details.class)
    @Override
    public String getMemo(){return super.getMemo();}

    @Column(name = "HEIGHT")
    @JsonView(Details.class)
    @Override
    public BigDecimal getHeight(){return super.getHeight();}




}
