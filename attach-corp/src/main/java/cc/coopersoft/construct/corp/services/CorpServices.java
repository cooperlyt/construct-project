package cc.coopersoft.construct.corp.services;


import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import cc.coopersoft.common.data.ConstructJoinType;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.construct.corp.model.*;
import cc.coopersoft.construct.corp.repository.CorpBusinessRepository;
import cc.coopersoft.construct.corp.repository.CorpRegRepository;
import cc.coopersoft.construct.corp.repository.CorpRepository;
import com.github.wujun234.uid.UidGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class CorpServices {

    private static final Logger logger = LoggerFactory.getLogger(CorpServices.class);

    private final static  int PAGE_SIZE = 20;

    private final CorpRepository corpRepository;

    private final CorpBusinessRepository corpBusinessRepository;

    private final CorpRegRepository corpRegRepository;

    @Resource
    private UidGenerator defaultUidGenerator;

    @Autowired
    public CorpServices(CorpRepository corpRepository,
                        CorpBusinessRepository corpBusinessRepository,
                        CorpRegRepository corpRegRepository) {
        this.corpRepository = corpRepository;
        this.corpBusinessRepository = corpBusinessRepository;
        this.corpRegRepository = corpRegRepository;
    }

    public Optional<CorpReg> corpReg(String corpCode, ConstructJoinType type){
        return corpRegRepository.findByIdCorpCorpCodeAndIdType(corpCode,type);
    }

    public Optional<CorpBusiness> corpBusiness(long businessId){
        return this.corpBusinessRepository.findById(businessId);
    }

    public Optional<Corp> corp(String corpCode){
        return this.corpRepository.findById(corpCode);
    }

    public List<CorpBusiness> listBusiness(String corpCode){
        return this.corpBusinessRepository.findByStatusInAndCorpInfoCorpCodeOrderByCreateTime(
                EnumSet.of(BusinessStatus.running,BusinessStatus.valid),corpCode);
    }


    public Page<Corp> listAllCorp(boolean validOnly, Optional<ConstructJoinType> joinType,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){
        //TODO StringUtils.split(key)

        Specification<Corp> specification = (Specification<Corp>) (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new LinkedList<>();

            Fetch<Corp, CorpInfo> infoFetch = root.fetch("info", JoinType.LEFT);
            Join<Corp,CorpInfo> infoJoin = (Join<Corp, CorpInfo>) infoFetch;
            if (key.isPresent() && StringUtils.isNotBlank(key.get())){
                String _key = key.get().trim();
                String _keyLike = "%" + _key + "%";
                predicates.add(cb.equal(root.get("corpCode").as(String.class),_key));
                predicates.add(cb.like(infoJoin.get("groupId").as(String.class), _keyLike ));
                predicates.add(cb.like(infoJoin.get("ownerName").as(String.class), _keyLike));
                predicates.add(cb.like(infoJoin.get("ownerId").as(String.class), _keyLike));
                predicates.add(cb.like(infoJoin.get("address").as(String.class), _keyLike));
                predicates.add(cb.like(infoJoin.get("tel").as(String.class),_keyLike));
                predicates.add(cb.like(infoJoin.get("name").as(String.class),_keyLike));
            }

            Predicate predicate = cb.or(predicates.toArray(new Predicate[predicates.size()]));


            if (joinType.isPresent()){

                Join<Corp,CorpReg> regJoin = root.join("regs", JoinType.INNER);
                predicate = cb.and(cb.equal(regJoin.get("id.CORP_TYPE").as(ConstructJoinType.class),joinType.get()) ,
                        predicate);

            }

            if (validOnly){
                predicate = cb.and(cb.isTrue(root.get("enable").as(Boolean.class)),predicate);
            }
            return predicate;

        };

        Sort sortable = Sort.by((dir.isPresent() ? ("DESC".equals(dir.get()) ? Sort.Direction.DESC : Sort.Direction.ASC) : Sort.Direction.DESC)
                , (sort.isPresent() ? sort.get() : "dataTime"));

        return corpRepository.findAll(specification, PageRequest.of(page.isPresent() ? page.get() : 0 ,PAGE_SIZE,sortable));
    }

    @Transactional
    public void setCorpEnable(String id, boolean enable){
        Optional<Corp> corp = this.corpRepository.findById(id);
        if (corp.isPresent()){
            corp.get().setEnable(enable);
            this.corpRepository.save(corp.get());
        }
    }


    @Transactional()
    public Corp patchCreate(CorpBusiness business){
        CorpBusiness regBusiness = createCorp(business);

        regBusiness.setRegTime(new Date());
        regBusiness.setApplyTime(new Date());
        regBusiness.setSource(BusinessSource.OLD);
        regBusiness.setStatus(BusinessStatus.valid);

        Corp corp = new Corp();
        corp.setInfo(regBusiness.getCorpInfo());
        corp.setCorpCode(regBusiness.getCorpInfo().getCorpCode());
        corp.setEnable(true);
        corp.setDataTime(new Date());

        String types = "";

        for(BusinessReg reg:  regBusiness.getRegs()){
            CorpReg corpReg = new CorpReg();
            corpReg.setId(new CorpRegPK(reg.getId().getType(),corp));
            corpReg.setInfo(reg.getInfo());
            corp.getRegs().add(corpReg);

            types = types + " " + reg.getId().getType().name();

            logger.debug("add record type :" + reg.getId().getType().name());
        }

        corp.setTypes(types.trim());


        this.corpBusinessRepository.save(regBusiness);
        return this.corpRepository.save(corp);
    }

    @Transactional
    public Corp patchModify(String corpCode, CorpBusiness business){

        Optional<Corp> _corp = this.corpRepository.findById(corpCode);
        Corp corp;
        if (_corp.isEmpty()){
            throw new IllegalArgumentException("机构不存在：" +  corpCode);
        }else{
            corp = _corp.get();
        }

        if (corpInBusiness(corpCode)){
            throw new IllegalArgumentException("机构业务正在处理：" +  corpCode);
        }

        CorpBusiness regBusiness = modifyCorp(corp,business);
        regBusiness.setRegTime(new Date());
        regBusiness.setApplyTime(new Date());
        regBusiness.setSource(BusinessSource.OLD);
        regBusiness.setStatus(BusinessStatus.valid);

        corp.setDataTime(new Date());

        if (regBusiness.isInfo()){
            corp.setInfo(regBusiness.getCorpInfo());
        }

        Map<ConstructJoinType, CorpReg> corpRegs = new HashMap<>(corp.getRegs().size());

        for(CorpReg reg: corp.getRegs()){
            corpRegs.put(reg.getId().getType(),reg);
        }

        String types = "";

        for(BusinessReg reg:  regBusiness.getRegs()){
            switch (reg.getOperateType()){
                case DELETE:
                    corp.getRegs().remove(corpRegs.get(reg.getId().getType()));
                    logger.debug("remove reg type: " + reg.getId().getType() + "; size is:" + corp.getRegs().size());
                    break;
                case MODIFY:
                    corp.getRegs().remove(corpRegs.get(reg.getId().getType()));
                case CREATE:
                    CorpReg corpReg = new CorpReg();
                    corpReg.setId(new CorpRegPK(reg.getId().getType(),corp));
                    corpReg.setInfo(reg.getInfo());
                    corp.getRegs().add(corpReg);
                    break;
            }
            if (!BusinessReg.OperateType.DELETE.equals(reg.getOperateType())){
                types = types + " " + reg.getId().getType().name();
            }
        }
        corp.setTypes(types.trim());


        this.corpBusinessRepository.save(regBusiness);
        return this.corpRepository.save(corp);
    }

    private boolean corpInBusiness(String corpCode){
        return this.corpBusinessRepository.existsByStatusAndCorpInfoCorpCode(BusinessStatus.running,corpCode);
    }

    private CorpBusiness modifyCorp(Corp corp, CorpBusiness regBusiness){

        regBusiness.setCreateTime(new Date());
        regBusiness.setId(defaultUidGenerator.getUID());

        if (regBusiness.getCorpInfo() != null){
            regBusiness.setInfo(true);
            regBusiness.getCorpInfo().setCorpCode(corp.getCorpCode());
            regBusiness.getCorpInfo().setPrevious(corp.getInfo());
            regBusiness.getCorpInfo().setId(defaultUidGenerator.getUID());
        }else{
            regBusiness.setInfo(false);
            regBusiness.setCorpInfo(corp.getInfo());
        }

        Map<ConstructJoinType, CorpReg> corpRegs = new HashMap<>(corp.getRegs().size());

        for(CorpReg reg: corp.getRegs()){
            corpRegs.put(reg.getId().getType(),reg);
        }

        for(BusinessReg reg:  regBusiness.getRegs()) {
            ConstructJoinType joinType = reg.getId().getType();
            switch (reg.getOperateType()){
                case QUOTED:
                    throw new IllegalArgumentException("操作类型错误：" +  reg.getOperateType());
                case DELETE:
                    reg.setInfo(corpRegs.get(joinType).getInfo());
                    break;
                case MODIFY:
                    logger.debug("add modify info level number:" + reg.getInfo().getLevelNumber());
                    reg.getInfo().setPrevious(corpRegs.get(joinType).getInfo());
                case CREATE:
                    reg.getInfo().setId(defaultUidGenerator.getUID());
                    break;
            }
            reg.getId().setBusiness(regBusiness);
            corpRegs.remove(joinType);
        }

        for(CorpReg corpReg : corpRegs.values()){
            BusinessReg businessReg = new BusinessReg(regBusiness,corpReg.getId().getType());
            businessReg.setOperateType(BusinessReg.OperateType.QUOTED);
            businessReg.setInfo(corpReg.getInfo());
            regBusiness.getRegs().add(businessReg);
        }
        return regBusiness;
    }

    private CorpBusiness createCorp(CorpBusiness regBusiness){

        String corpCode;
        if (GroupIdType.COMPANY_CODE.equals(regBusiness.getCorpInfo().getGroupIdType())){
            corpCode = regBusiness.getCorpInfo().getGroupId();
            if (corpRepository.existsById(corpCode)){
                throw new IllegalArgumentException("机构已经存在：" +  corpCode);
            }
        }else{
            corpCode = defaultUidGenerator.parseUID(defaultUidGenerator.getUID());
        }


        regBusiness.setCreateTime(new Date());
        regBusiness.setId(defaultUidGenerator.getUID());
        regBusiness.setInfo(true);


        regBusiness.getCorpInfo().setId(defaultUidGenerator.getUID());
        regBusiness.getCorpInfo().setCorpCode(corpCode);
        regBusiness.getCorpInfo().setPrevious(null);

        for(BusinessReg reg:  regBusiness.getRegs()){
            reg.getInfo().setId(defaultUidGenerator.getUID());
            reg.getId().setBusiness(regBusiness);
            reg.setOperateType(BusinessReg.OperateType.CREATE);
            reg.getInfo().setPrevious(null);
        }
        return regBusiness;
    }





}
