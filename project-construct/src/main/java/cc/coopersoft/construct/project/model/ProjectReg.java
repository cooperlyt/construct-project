package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PROJECT_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "reg.full",
        attributeNodes = {@NamedAttributeNode("apply"),@NamedAttributeNode("info")})
public class ProjectReg {


    public interface Summary extends ProjectInfo.Summary {}
    public interface SummaryWithReg extends Summary {}
    public interface DetailsWithReg extends SummaryWithReg, ProjectInfo.Details {}
    public interface DetailsWithCorp extends Summary, ProjectInfo.DetailsWithCorp {}

    @Id
    @Column(name = "BUSINESS_ID", unique = true, nullable = false)
    @JsonView(Summary.class)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    @JsonIgnore
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME")
    @JsonView(Summary.class)
    private Date applyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView({DetailsWithReg.class, DetailsWithCorp.class})
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    @JsonView(Summary.class)
    private BusinessSource source;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(Summary.class)
    private BusinessStatus status;

    @Column(name = "TAGS", length = 512)
    @JsonIgnore
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLY_CORP", nullable = false)
    @JsonView(SummaryWithReg.class)
    private JoinCorp apply;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private ProjectReg previous;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL)
    @JsonView(SummaryWithReg.class)
    private ProjectInfo info;

}
