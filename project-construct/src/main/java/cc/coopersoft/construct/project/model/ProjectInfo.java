package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ProjectInfo implements java.io.Serializable{

    @Id
    @Column(name = "BUSINESS_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "PROJECT_CODE", nullable = false, length = 32)
    private String projectCode;

    @Column(name = "NAME", nullable = false, length = 256)
    @NotBlank
    private String name;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    //TODO Enum maybe not null
    @Column(name = "TYPE", length = 32)
    private String type;

    //TODO Enum maybe not null
    @Column(name = "PROPERTY", length = 32)
    private String property;

    @Column(name = "CONTRACT_AREA")
    private BigDecimal area;

    @Column(name = "ALL_AREA")
    private BigDecimal landArea;

    @Column(name = "GROUND_FLOOR_COUNT")
    private Integer groundCount;

    @Column(name = "UNDER_FLOOR_COUNT")
    private Integer underCount;

    @Temporal(TemporalType.DATE)
    @Column(name = "BEGIN_DATE")
    private Date BeginDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "COMPLETED_DATE")
    private Date completeDate;

    //TODO Enum
    @Column(name = "TENDER", length = 32)
    private String tender;

    //TODO Enum
    @Column(name = "STRUCTURE", length = 32)
    private String structure;

    @Column(name = "COSTS")
    private BigDecimal costs;

    @Column(name = "MAIN_PROJECT")
    private Boolean important;

    //TODO Enum or int
    @Column(name = "MAIN_PROJECT_LEVEL", length = 16)
    private String importantLevel;

    @Column(name = "MAIN_PROJECT_FILE", length = 32)
    private String importantFile;

    @Column(name = "MEMO", length = 512)
    private String memo;

    @Column(name = "HEIGHT")
    private BigDecimal height;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "info", cascade = CascadeType.ALL)
    private Set<JoinCorp> corps = new HashSet<>(0);

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    @MapsId
    private ProjectReg reg;

}
