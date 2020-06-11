package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.data.PersonIdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONSTRUCT_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class CorpEmployee {

    @Id
    @Column(name = "ID",nullable = false , unique = true, length = 32)
    private String id;

    @Column(name = "NAME", length = 64, nullable = false)
    private String name;

    @Column(name = "TEL", length = 16)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(name = "IDENTIFY_TYPE", length = 16, nullable = false)
    private PersonIdType idType;

    @Column(name = "IDENTIFY_NUMBER", length = 32)
    private String idNumber;

    @Column(name = "DATA_TIME", nullable = false)
    private Date dataTime;

    @Column(name = "MANAGER", nullable = false)
    private boolean manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORP_CODE", nullable = false)
    @JsonIgnore
    private Corp corp;


}
