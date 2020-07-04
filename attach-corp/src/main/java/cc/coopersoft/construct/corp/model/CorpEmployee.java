package cc.coopersoft.construct.corp.model;


import cc.coopersoft.common.data.PersonIdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "CONSTRUCT_EMPLOYEE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "employee.full",
        attributeNodes = {
                @NamedAttributeNode(value ="corp", subgraph = "corp.info")} ,
        subgraphs = {
                @NamedSubgraph(name = "corp.info", attributeNodes = @NamedAttributeNode("info"))
        }
)
public class CorpEmployee {

    @Id
    @Column(name = "ID",nullable = false , unique = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "VALID", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean valid;

    @Column(name = "USERNAME", length = 32, nullable = false)
    @Size(max = 32)
    private String username;


    @Column(name = "NAME", length = 32, nullable = false)
    @NotBlank
    @Size(max = 32)
    private String name;

    @Column(name = "TEL", length = 16)
    @NotBlank
    @Size(max = 16)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "IDENTIFY_TYPE", length = 16, nullable = false)
    @NotNull
    private PersonIdType idType;

    @Column(name = "IDENTIFY_NUMBER", length = 32)
    @Size(max = 32)
    private String idNumber;

    @Column(name = "DATA_TIME", nullable = false)
    @JsonIgnore
    private Date dataTime;

    @Column(name = "MANAGER", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORP_CODE", nullable = false)
    @JsonIgnore
    private Corp corp;


}
