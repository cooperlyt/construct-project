package cc.coopersoft.construct.corp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CONSTRUCT_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class Corp {

    @Id
    @Column(name = "CORP_CODE", length = 32 ,nullable = false, unique = true)
    private String corpCode;

    @Column(name = "ENABLE", nullable = false)
    private boolean enable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP_INFO", nullable = false)
    private CorpInfo info;

    @Column(name = "TYPES", nullable = false, length = 128)
    private String types;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    private Date dataTime;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.corp", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<CorpReg> regs = new HashSet<>(0);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (corpCode == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        Corp corp = (Corp) o;

        return Objects.equals(corpCode, corp.corpCode);
    }

    @Override
    public int hashCode() {
        return corpCode != null ? corpCode.hashCode() : super.hashCode();
    }
}
