package cc.coopersoft.construct.project.model;

import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
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
//@NamedEntityGraph(name = "reg.summary",
//        attributeNodes = {@NamedAttributeNode("info")})
public class ProjectReg implements java.io.Serializable{


    public interface Title {}
    public interface Summary extends Title, ProjectInfoReg.Summary, ProjectCorpReg.Summary {}
    public interface Details extends Title, ProjectInfoReg.Details, ProjectCorpReg.Details {}


    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonView(Title.class)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    @JsonIgnore
    private Date createTime;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(Title.class)
    private RegStatus status;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TAGS", length = 512)
    @JsonIgnore
    private String tags;

    @Column(name = "PROJECT_CODE", nullable = false)
    @JsonView(Title.class)
    private long code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME")
    @JsonView(Title.class)
    private Date applyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(Title.class)
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(Title.class)
    private RegSource source;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST , CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "CORP", nullable = false)
    @JsonView(Title.class)
    private ProjectCorpReg corp;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST , CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "INFO", nullable = false)
    @JsonView(Title.class)
    private ProjectInfoReg info;

    @Column(name = "CORP_MASTER",nullable = false)
    @JsonView(Details.class)
    private boolean corpMaster;

    @Column(name = "INFO_MASTER", nullable = false)
    @JsonView(Details.class)
    private boolean infoMaster;


}
