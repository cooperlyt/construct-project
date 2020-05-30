package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BUILD_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class BuildReg implements java.io.Serializable{

    public interface Summary {}

    public interface Details extends BuildRegInfo.View {}

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @JsonView({Summary.class,Details.class})
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView({Summary.class, Details.class})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date regTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    @JsonManagedReference
    @JsonView(Details.class)
    private List<BuildRegInfo> builds = new ArrayList<>(0);

    @Column(name = "COUNT", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Summary.class)
    private int count;

    @Column(name = "ON_AREA", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Summary.class)
    private BigDecimal onArea;

    @Column(name = "UNDER_AREA", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Summary.class)
    private BigDecimal underArea;

}
