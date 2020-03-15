package cc.coopersoft.construct.corp.model;

import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.PersonIdType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "CORP_INFO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class CorpInfo implements java.io.Serializable {

    @Id
    private Long id;

    @Column(name = "CORP_CODE", nullable = false, length = 32)
    private String corpCode;


    @Column(name = "NAME", length = 128, nullable = false)
    @NotBlank
    @Size(max = 128)
    private String name;

    @Column(name = "REG_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupIdType groupIdType;

    @Column(name = "REG_ID_NUMBER", nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String groupId;

    @Column(name = "OWNER_NAME", nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String ownerName;

    @Column(name = "OWNER_ID_TYPE", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    @NotNull
    private PersonIdType ownerIdType;

    @Column(name = "OWNER_ID_NUMBER", length = 32, nullable = false)
    @NotBlank
    @Size(max = 32)
    private String ownerId;

    @Column(name = "ADDRESS", length = 256)
    @Size(max = 256)
    private String address;

    @Column(name = "TEL", length = 16)
    @Size(max = 16)
    private String tel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PREVIOUS")
    private CorpInfo previous;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.id == null) return false;
        if (o == null || getClass() != o.getClass()) return false;

        CorpInfo corpInfo = (CorpInfo) o;

        return Objects.equals(id, corpInfo.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
