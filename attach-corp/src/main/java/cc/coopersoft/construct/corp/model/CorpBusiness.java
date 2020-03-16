package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CorpBusiness {

    @Id
    @Column(name = "BUSINESS_ID", nullable = false, unique = true)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = false)
    private Date createTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "REG_DATE")
    private Date regDate;

    @Column(name = "SOURCE", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private BusinessSource source;

    @Column(name = "STATUS", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private BusinessStatus status;

    @Column(name = "TAGS", length = 512)
    private String tags;

    @Column(name = "INFO", nullable = false)
    private boolean info;

    @ManyToOne(fetch = FetchType.LAZY,optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "CORP_INFO", nullable = false)
    private CorpInfo corpInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business", cascade = CascadeType.ALL)
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
