package cc.coopersoft.construct.corp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "CREDIT_RECORD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class CreditRecord  {

    public enum Type {
        Break,
        Import
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 16, nullable = false)
    @NotNull
    private Type type;

    @Column(name = "DESCRIPTION", length = 512)
    @Size(max = 512)
    private String description;

    @Column(name = "RECORD_TIME", nullable = false)
    private Date recordTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CORP_CODE", nullable = false)
    @JsonIgnore
    private Corp corp;
}
