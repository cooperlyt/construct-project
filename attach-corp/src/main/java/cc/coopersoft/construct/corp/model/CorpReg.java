package cc.coopersoft.construct.corp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CONSTRUCT_CORP_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class CorpReg {

    @EmbeddedId
    private CorpRegPK id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG_ID", nullable = false)
    private RegInfo info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorpReg corpReg = (CorpReg) o;

        return Objects.equals(id, corpReg.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
