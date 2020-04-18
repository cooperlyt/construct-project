package cc.coopersoft.construct.project.model;

import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.common.json.JsonRawDeserializer;
import cc.coopersoft.common.json.JsonRawSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PROJECT_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "reg.summary",
        attributeNodes = {@NamedAttributeNode("info")})
public class ProjectReg extends cc.coopersoft.common.construct.project.ProjectReg<ProjectInfo,JoinCorp>{


    public interface BaseView extends ProjectInfo.Summary {}

    public interface Summary extends BaseView {}
    public interface Details extends BaseView, ProjectInfo.Details, JoinCorp.Details {}

    @Id
    @Column(name = "BUSINESS_ID", unique = true, nullable = false)
    @JsonView(BaseView.class)
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    @JsonIgnore
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPLY_TIME")
    @JsonView(BaseView.class)
    private Date applyTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(BaseView.class)
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    @JsonView(BaseView.class)
    private RegSource source;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private ProjectReg previous;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "INFO", nullable = false)
    @JsonView(BaseView.class)
    private ProjectInfo info;

    @JsonSerialize(using = JsonRawSerialize.class)
    @JsonDeserialize(using = JsonRawDeserializer.class)
    @Column(name = "CORPS", length = 512)
    @JsonView(Summary.class)
    private String corpDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL)
    @JsonView(Details.class)
    private Set<JoinCorp> corps = new HashSet<>(0);

}
