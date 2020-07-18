package cc.coopersoft.construct.project.model;



import cc.coopersoft.common.cloud.json.JsonRawDeserializer;
import cc.coopersoft.common.cloud.json.JsonRawSerialize;
import cc.coopersoft.common.construct.project.ProjectCorpSummary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "PROJECT_JOIN_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class JoinCorpReg implements ProjectCorpSummary {

    public interface Summary {}
    public interface SummaryWithCorp extends  Project.Title {}

    public interface Details extends JoinCorp.Details {}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private JoinCorpReg previous;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TAGS", length = 1024)
    @JsonIgnore
    private String corpTags;

    @Column(name = "CORPS")
    @JsonDeserialize(using = JsonRawDeserializer.class)
    @JsonSerialize(using = JsonRawSerialize.class)
    @JsonView(Summary.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String corpSummary;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "corp")
    @JsonView(SummaryWithCorp.class)
    private Project project;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({Summary.class, SummaryWithCorp.class, Details.class})
    private Long id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({Summary.class, SummaryWithCorp.class, Details.class })
    private Date regTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("property,id")
    @JsonView(Details.class)
    private List<JoinCorp> corps = new ArrayList<>(0);


}
