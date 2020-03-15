package cc.coopersoft.construct.corp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRegPK implements java.io.Serializable {


    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "REG_ID", nullable = false)
    private RegInfo info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    private CorpBusiness business;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessRegPK that = (BusinessRegPK) o;

        if (info == null || that.info == null) return false;
        if (business == null || that.business == null) return false;

        if (!Objects.equals(info, that.info)) return false;
        return Objects.equals(business, that.business);
    }

    @Override
    public int hashCode() {
        if (info == null || business == null) return super.hashCode();
        int result = info != null ? info.hashCode() : 0;
        result = 31 * result + (business != null ? business.hashCode() : 0);
        return result;
    }
}
