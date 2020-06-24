package cc.coopersoft.construct.corp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
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
@Access(AccessType.PROPERTY)
public class RegInfo extends cc.coopersoft.common.construct.corp.RegInfo{

    public interface Details{}

    @Id
    @Column(name = "ID",nullable = false, unique = true)
    @JsonIgnore
    @Access(AccessType.FIELD)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @Access(AccessType.FIELD)
    private RegInfo previous;

    @Temporal(TemporalType.DATE)
    @Column(name = "REG_DATE_TO")
    @JsonView(Details.class)
    @Override
    public Date getRegTo(){
        return super.getRegTo();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME")
    @JsonView(Details.class)
    @Override
    public Date getRegTime(){
        return super.getRegTime();
    }

    @Column(name = "LEVEL")
    @JsonView(Details.class)
    @Override
    public int getLevel(){
        return super.getLevel();
    }

    @Column(name = "LEVEL_NUMBER", length = 32)
    @JsonView(Details.class)
    @Override
    public String getLevelNumber(){
        return super.getLevelNumber();
    }



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
