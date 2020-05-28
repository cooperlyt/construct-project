package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BUILD_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class BuildReg implements java.io.Serializable{

    //当前没有使用,但如果有其它描述信息可以启用
    public interface Summary {}

    public interface Details extends Summary, BuildRegInfo.View {}

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @JsonIgnore
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(Summary.class)
    private Date regTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reg", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    @JsonView(Details.class)
    @JsonManagedReference
    private List<BuildRegInfo> builds = new ArrayList<>(0);

}
