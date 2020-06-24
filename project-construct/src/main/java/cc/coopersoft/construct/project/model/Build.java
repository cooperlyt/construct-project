package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BUILD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class Build extends cc.coopersoft.common.construct.project.Build<BuildInfo> implements java.io.Serializable {

    public interface View extends BuildInfo.View {}

    @Id
    @Column(name = "BUILD_CODE", nullable = false, unique = true)
    @JsonView(View.class)
    @Override
    public long getCode(){return super.getCode();}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REG_TIME", nullable = false)
    @JsonView(View.class)
    @Override
    public Date getRegTime(){return super.getRegTime();}

    @Column(name = "ENABLE", nullable = false)
    @JsonView(View.class)
    @Override
    public boolean isEnable(){return super.isEnable();}

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INFO", nullable = false)
    @JsonView(View.class)
    @Override
    public BuildInfo getInfo(){return super.getInfo();}

    @Column(name = "PROJECT_CODE", nullable = false)
    @JsonView(View.class)
    @Override
    public long getProjectCode(){return super.getProjectCode();}

    @Version
    @Column(name = "VERSION")
    @JsonIgnore
    private Integer version;

}
