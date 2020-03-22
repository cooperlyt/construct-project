package cc.coopersoft.construct.corp.model;


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
@Table(name = "CONSTRUCT_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "corp.full", attributeNodes = {@NamedAttributeNode(("info"))})
public class Corp {

    public interface Summary {}
    public interface Details extends CorpBusiness.Summary {}

    @Id
    @Column(name = "CORP_CODE", length = 32 ,nullable = false, unique = true)
    @JsonView(Summary.class)
    private String corpCode;

    @Column(name = "ENABLE", nullable = false)
    @JsonView(Summary.class)
    private boolean enable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP_INFO", nullable = false)
    @JsonView(Summary.class)
    private CorpInfo info;

    @Column(name = "TYPES", nullable = false, length = 128)
    @JsonView(Summary.class)
    private String types;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    @JsonIgnore
    private Date dataTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.corp", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonView(Details.class)
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
