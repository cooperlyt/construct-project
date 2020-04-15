package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONSTRUCT_PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "project.summary",
        attributeNodes = {@NamedAttributeNode(value = "reg", subgraph = "reg.info")} ,
        subgraphs = {@NamedSubgraph(name = "reg.info", attributeNodes = @NamedAttributeNode("info"))}
)
public class Project {

    public interface Summary extends ProjectReg.Summary {}
    public interface Details extends Summary, ProjectReg.Details {}

    @Id
    @Column(name = "PROJECT_CODE", nullable = false, unique = true)
    @JsonView(Summary.class)
    private Long code;

    @Column(name = "ENABLE", nullable = false)
    @JsonView(Summary.class)
    private boolean enable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    @JsonIgnore
    private Date dataTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "REG", nullable = false)
    @JsonView(Summary.class)
    private ProjectReg reg;


}
