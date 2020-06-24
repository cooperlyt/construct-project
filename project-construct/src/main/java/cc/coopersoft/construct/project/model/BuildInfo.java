package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "BUILD_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@Access(AccessType.PROPERTY)
public class BuildInfo  extends cc.coopersoft.common.construct.project.BuildInfo implements java.io.Serializable{

    public interface View {}

    @Id
    @Access(AccessType.FIELD)
    @Column(name = "ID", nullable = false, unique = true)
    @JsonIgnore
    private Long id;

    @Column(name = "NAME", length = 32, nullable = false)
    @Size(max = 32)
    @NotBlank
    @JsonView(View.class)
    @Override
    public String getName(){return super.getName();}

    @Column(name = "GROUND_AREA")
    @JsonView(View.class)
    @Override
    public BigDecimal getOnArea(){return super.getOnArea();}

    @Column(name = "UNDER_AREA")
    @JsonView(View.class)
    @Override
    public BigDecimal getUnderArea(){return super.getUnderArea();}

    @Column(name = "ALL_AREA")
    @JsonView(View.class)
    @Override
    public BigDecimal getLandArea(){return super.getLandArea();}

    @Column(name = "GROUND_FLOOR_COUNT")
    @JsonView(View.class)
    @Override
    public Integer getOnCount(){return super.getOnCount();}

    @Column(name = "UNDER_FLOOR_COUNT")
    @JsonView(View.class)
    @Override
    public Integer getUnderCount(){return super.getUnderCount();}

    @Enumerated(EnumType.STRING)
    @Column(name = "STRUCTURE", length = 16)
    @NotNull
    @JsonView(View.class)
    @Override
    public Struct getStructure(){return super.getStructure();}

    @Column(name = "HEIGHT")
    @JsonView(View.class)
    @Override
    public BigDecimal getHeight(){return super.getHeight();}
}
