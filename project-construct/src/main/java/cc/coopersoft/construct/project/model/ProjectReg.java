package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PROJECT_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class ProjectReg {


    @Id
    @Column(name = "BUSINESS_ID", unique = true, nullable = false)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME")
    private Date applyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private BusinessSource source;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private BusinessStatus status;

    @Column(name = "TAGS", length = 512)
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLY_CORP", nullable = false)
    private JoinCorp apply;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    private ProjectReg previous;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL)
    private ProjectInfo info;

}
