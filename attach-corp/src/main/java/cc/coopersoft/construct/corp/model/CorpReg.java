package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.construct.corp.CorpProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CONSTRUCT_CORP_REG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class CorpReg extends cc.coopersoft.common.construct.corp.CorpReg<RegInfo>{

    public interface Details extends RegInfo.Details {}

    @EmbeddedId
    @JsonIgnore
    private CorpRegPK id = new CorpRegPK();


    @Transient
    @Override
    @JsonView({Details.class, Corp.TitleWithReg.class})
    @Access(AccessType.PROPERTY)
    public CorpProperty getProperty(){
        return this.id.getProperty();
    }

    @Override
    public void setProperty(CorpProperty property){
        this.id.setProperty(property);
    }



    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG_ID", nullable = false)
    @JsonView(Details.class)
    @Override
    @Access(AccessType.PROPERTY)
    public RegInfo getInfo(){
        return super.getInfo();
    }

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
