package cc.coopersoft.construct.corp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CONSTRUCT_CORP")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "corp.full", attributeNodes = {@NamedAttributeNode(("info"))})
public class Corp extends cc.coopersoft.common.construct.corp.Corp<CorpInfo,CorpReg>{

    public interface Summary extends CorpInfo.Summary{}
    public interface Details extends Summary, CorpReg.Details, CorpInfo.Details {}


    @Column(name = "TYPES", nullable = false, length = 128)
    @JsonView(Summary.class)
    @Access(AccessType.FIELD)
    private String types;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Date dataTime;


    @Id
    @Column(name = "CORP_CODE", length = 32 ,nullable = false, unique = true)
    @JsonView(Summary.class)
    @Override
    public Long getCode(){
        return super.getCode();
    }

    @Column(name = "ENABLE", nullable = false)
    @JsonView(Summary.class)
    @Override
    public boolean isEnable(){
        return super.isEnable();
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP_INFO", nullable = false)
    @JsonView(Summary.class)
    @Override
    public CorpInfo getInfo(){
        return super.getInfo();
    }



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.corp", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonView(Details.class)
    @Override
    public Set<CorpReg> getRegs(){
        return super.getRegs();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getCode() == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        Corp corp = (Corp) o;

        return Objects.equals(getCode(), corp.getCode());
    }

    @Override
    public int hashCode() {
        return getCode() != null ? getCode().hashCode() : super.hashCode();
    }
}
