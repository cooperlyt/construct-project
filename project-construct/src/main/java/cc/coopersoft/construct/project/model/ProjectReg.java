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


    public interface BaseView extends ProjectInfo.Summary {}

    public interface Summary extends BaseView {}
    public interface Details extends BaseView, ProjectInfo.Details, JoinCorp.Details {}


    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonView(BaseView.class)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    @JsonIgnore
    private Date createTime;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(BaseView.class)
    private RegStatus status;

    @Column(name = "TAGS", length = 512)
    @JsonIgnore
    private String tags;

    @Column(name = "PROJECT_CODE", nullable = false)
    @JsonView(BaseView.class)
    private long code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME")
    @JsonView(BaseView.class)
    private Date applyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(BaseView.class)
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(BaseView.class)
    private RegSource source;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @PrimaryKeyJoinColumn
    private ProjectCorpReg corp;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @PrimaryKeyJoinColumn
    private ProjectInfoReg info;

    @Column(name = "CORP_MASTER",nullable = false)
    @JsonView(Details.class)
    private boolean corpMaster;

    @Column(name = "INFO_MASTER", nullable = false)
    @JsonView(Details.class)
    private boolean infoMaster;


}
