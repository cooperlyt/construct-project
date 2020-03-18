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
public class BusinessRegPK implements java.io.Serializable{

    @Column(name = "CORP_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ConstructJoinType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    private CorpBusiness business;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.business == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessRegPK that = (BusinessRegPK) o;

        if (type != that.type) return false;
        return Objects.equals(business, that.business);
    }

    @Override
    public int hashCode() {
        if (this.business == null) return super.hashCode();
        int result = type.hashCode();
        result = 31 * result + (business != null ? business.hashCode() : 0);
        return result;
    }
}
