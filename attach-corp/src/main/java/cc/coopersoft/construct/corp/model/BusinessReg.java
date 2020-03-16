package cc.coopersoft.construct.corp.model;


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

    @Id
    @Column(name = "ID" , nullable = false, unique = true)
    private Long id;

    @Column(name = "OPERATE", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateType operateType;


    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "REG_ID", nullable = false)
    private RegInfo info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    private CorpBusiness business;

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
