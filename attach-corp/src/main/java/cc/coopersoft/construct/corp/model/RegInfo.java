package cc.coopersoft.construct.corp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "CONSTRUCT_CORP_REG_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class RegInfo {

    @Id
    @Column(name = "ID",nullable = false, unique = true)
    private Long id;

    @Column(name = "REG_DATE_TO")
    private Date regTo;

    @Column(name = "LEVEL")
    private int level;

    @Column(name = "LEVEL_NUMBER", length = 32)
    private String levelNumber;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    private RegInfo previous;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.id == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        RegInfo regInfo = (RegInfo) o;

        return Objects.equals(id, regInfo.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();

    }
}
