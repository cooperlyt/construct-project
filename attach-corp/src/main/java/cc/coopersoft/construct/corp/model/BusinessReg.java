package cc.coopersoft.construct.corp.model;


import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

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
    private BusinessRegPK id;

    @Column(name = "OPERATE", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateType operateType;


    public BusinessReg(BusinessRegPK id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessReg that = (BusinessReg) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
