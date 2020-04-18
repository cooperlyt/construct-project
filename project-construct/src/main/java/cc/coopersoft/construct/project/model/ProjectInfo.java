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
public class ProjectInfo extends cc.coopersoft.common.construct.project.ProjectInfo implements java.io.Serializable{

    public interface Summary {}
    public interface Details extends Summary {}


    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonIgnore
    private Long id;


    @Column(name = "NAME", nullable = false, length = 256)
    @NotBlank
    @JsonView(Summary.class)
    private String name;

    @Column(name = "ADDRESS", length = 512)
    @JsonView(Summary.class)
    private String address;

    //TODO Enum maybe not null
    @Column(name = "TYPE", length = 32)
    @JsonView(Summary.class)
    private String type;

    //TODO Enum maybe not null
    @Column(name = "PROPERTY", length = 32)
    @JsonView(Summary.class)
    private String property;

    @Column(name = "CONTRACT_AREA")
    @JsonView(Summary.class)
    private BigDecimal area;

    @Column(name = "ALL_AREA")
    @JsonView(Summary.class)
    private BigDecimal landArea;

    @Column(name = "GROUND_FLOOR_COUNT")
    @JsonView(Summary.class)
    private Integer groundCount;

    @Column(name = "UNDER_FLOOR_COUNT")
    @JsonView(Summary.class)
    private Integer underCount;

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    @JsonView(Summary.class)
    private Date BeginDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPLETED_DATE")
    @JsonView(Summary.class)
    private Date completeDate;

    //TODO Enum
    @Column(name = "TENDER", length = 32)
    @JsonView(Details.class)
    private String tender;

    //TODO Enum
    @Column(name = "STRUCTURE", length = 32)
    @JsonView(Summary.class)
    private String structure;

    @Column(name = "COSTS")
    @JsonView(Summary.class)
    private BigDecimal costs;

    @Column(name = "MAIN_PROJECT")
    @JsonView(Summary.class)
    private Boolean important;

    //TODO Enum or int
    @Column(name = "MAIN_PROJECT_LEVEL", length = 16)
    @JsonView(Summary.class)
    private String importantLevel;

    @Column(name = "MAIN_PROJECT_FILE", length = 32)
    @JsonView(Details.class)
    private String importantFile;

    @Column(name = "MEMO", length = 512)
    @JsonView(Details.class)
    private String memo;

    @Column(name = "HEIGHT")
    @JsonView(Details.class)
    private BigDecimal height;




}
