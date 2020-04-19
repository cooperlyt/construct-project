package cc.coopersoft.construct.project.model;

import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.data.GroupIdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CorpSummary {

    private CorpProperty property;

    private long code;

    private String name;

    private GroupIdType idType;

    private String id;

}
