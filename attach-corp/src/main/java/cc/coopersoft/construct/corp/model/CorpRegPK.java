package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.data.ConstructJoinType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorpRegPK implements java.io.Serializable{

    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ConstructJoinType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name ="CORP_CODE", nullable = false)
    private Corp corp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorpRegPK corpRegPK = (CorpRegPK) o;


        if (type != corpRegPK.type) return false;
        return Objects.equals(corp, corpRegPK.corp);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (corp != null ? corp.hashCode() : 0);
        return result;
    }
}
