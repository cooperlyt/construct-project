package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CORP_REG_BUSINESS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "business.full", attributeNodes = {@NamedAttributeNode("corpInfo")})
public class CorpBusiness {

    public interface Summary {}
    public interface Details extends Summary {}

    @Id
    @Column(name = "BUSINESS_ID", nullable = false, unique = true)
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
    @JsonView(Details.class)
    private Date regTime;

    @Column(name = "SOURCE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    @JsonView(Summary.class)
    private BusinessSource source;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    @JsonView(Summary.class)
    private BusinessStatus status;

    @JsonIgnore
    @Column(name = "TAGS", length = 512)
    private String tags;

    @Column(name = "INFO", nullable = false)
    @JsonView(Details.class)
    private boolean info;

    @ManyToOne(fetch = FetchType.LAZY,optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "CORP_INFO", nullable = false)
    @JsonView(Summary.class)
    private CorpInfo corpInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.business", cascade = CascadeType.ALL)
    @JsonView(Details.class)
    private Set<BusinessReg> regs = new HashSet<>(0);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (id == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        CorpBusiness business = (CorpBusiness) o;

        return Objects.equals(id, business.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
