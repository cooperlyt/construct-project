package cc.coopersoft.construct.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONSTRUCT_PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class Project {

    @Id
    @Column(name = "PROJECT_CODE", nullable = false, unique = true)
    private String projectCode;

    @Column(name = "ENABLE", nullable = false)
    private boolean enable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_TIME", nullable = false)
    private Date dataTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_ID", nullable = false)
    private ProjectBusiness info;

}
