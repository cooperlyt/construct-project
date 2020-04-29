package cc.coopersoft.construct.corp.model;


import cc.coopersoft.common.construct.corp.CorpProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BUSINESS_REG")
public class BusinessReg {

    public enum OperateType{
        DELETE,
        MODIFY,
        CREATE,
        QUOTED
    }

    @EmbeddedId
    private BusinessRegPK id = new BusinessRegPK();

    @Transient
    @Access(AccessType.FIELD)
    public CorpProperty getProperty(){
        return id.getProperty();
    }

    public void setProperty(CorpProperty property){
        this.id.setProperty(property);
    }

    @Column(name = "OPERATE", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateType operateType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "REG_ID", nullable = false)
    private RegInfo info;

    public BusinessReg(CorpBusiness business, CorpProperty type) {
        this.id = new BusinessRegPK(type, business);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (id == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessReg that = (BusinessReg) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
