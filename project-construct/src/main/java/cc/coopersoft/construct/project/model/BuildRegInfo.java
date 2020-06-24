package cc.coopersoft.construct.project.model;


import cc.coopersoft.common.data.OperationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "BUILD_REG_AND_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class BuildRegInfo extends cc.coopersoft.common.construct.project.BuildRegInfo<BuildInfo> implements java.io.Serializable{

    public interface View extends BuildInfo.View {}

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATE", nullable = false, length = 6)
    @NotNull
    private OperationType operation;

    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "BUILD_INFO", nullable = false)
    @NotNull
    @JsonView(View.class)
    @Override
    public BuildInfo getInfo(){return super.getInfo();}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    @JsonIgnore
    private BuildInfo previous;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REG", nullable = false)
    @JsonBackReference
    private BuildReg reg;


    @Access(AccessType.PROPERTY)
    @Column(name = "BUILD_CODE", nullable = false)
    @JsonView(View.class)
    @Override
    public long getCode(){return super.getCode();}


}
