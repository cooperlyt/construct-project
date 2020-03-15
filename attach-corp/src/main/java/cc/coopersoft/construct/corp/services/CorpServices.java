package cc.coopersoft.construct.corp.services;


import cc.coopersoft.common.business.BusinessSource;
import cc.coopersoft.common.business.BusinessStatus;
import cc.coopersoft.common.data.ConstructJoinType;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.construct.corp.model.*;
import cc.coopersoft.construct.corp.repository.CorpBusinessRepository;
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

    @Resource
    private UidGenerator defaultUidGenerator;

    @Autowired
    public CorpServices(CorpRepository corpRepository,
                        CorpBusinessRepository corpBusinessRepository) {
        this.corpRepository = corpRepository;
        this.corpBusinessRepository = corpBusinessRepository;
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
    public CorpBusiness patchCreate(CorpBusiness business){
        CorpBusiness regBusiness = createCorp(business);

        regBusiness.setRegDate(new Date());
        regBusiness.setSource(BusinessSource.OLD);
        regBusiness.setStatus(BusinessStatus.valid);

        Corp corp = new Corp();
        corp.setInfo(regBusiness.getCorpInfo());
        corp.setCorpCode(regBusiness.getCorpInfo().getCorpCode());
        corp.setEnable(true);
        corp.setDataTime(new Date());

        String types = "";

        for(BusinessReg reg:  regBusiness.getRegs()){

            RegInfo regInfo = reg.getId().getInfo();

            CorpReg corpReg = new CorpReg();
            corpReg.setId(new CorpRegPK(regInfo.getType(),corp));
            corpReg.setInfo(regInfo);
            corp.getRegs().add(corpReg);

            types = types + " " + regInfo.getType().name();

            logger.debug("add record type :" + regInfo.getType().name());
        }

        corp.setTypes(types.trim());

        this.corpBusinessRepository.save(regBusiness);
        this.corpRepository.save(corp);

        return regBusiness;
    }

    @Transactional
    public CorpBusiness patchModify(String corpCode, CorpBusiness business){

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
        regBusiness.setRegDate(new Date());
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
            RegInfo businessRegInfo = reg.getId().getInfo();
            switch (reg.getOperateType()){
                case DELETE:
                    corp.getRegs().remove(corpRegs.get(reg.getOperateType()));
                    break;
                case CREATE:
                    CorpReg corpReg = new CorpReg();
                    corpReg.setId(new CorpRegPK(businessRegInfo.getType(),corp));
                    corpReg.setInfo(businessRegInfo);
                    corp.getRegs().add(corpReg);
                    break;
                case MODIFY:
                    corpRegs.get(reg.getOperateType()).setInfo(businessRegInfo);
                    break;
            }
            if (!BusinessReg.OperateType.DELETE.equals(reg.getOperateType())){
                types = types + " " + businessRegInfo.getType().name();
            }
        }
        corp.setTypes(types.trim());

        this.corpBusinessRepository.save(regBusiness);
        this.corpRepository.save(corp);

        return regBusiness;
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

        Map<ConstructJoinType, RegInfo> corpRegs = new HashMap<>(corp.getRegs().size());

        for(CorpReg reg: corp.getRegs()){
            corpRegs.put(reg.getId().getType(),reg.getInfo());
        }

        for(BusinessReg reg:  regBusiness.getRegs()) {
            reg.getId().setBusiness(regBusiness);
            if (!BusinessReg.OperateType.QUOTED.equals(reg.getOperateType())){
                throw new IllegalArgumentException("操作类型错误：" +  reg.getOperateType());
            }
            reg.getId().getInfo().setId(defaultUidGenerator.getUID());
            ConstructJoinType joinType = reg.getId().getInfo().getType();
            reg.getId().getInfo().setPrevious(corpRegs.get(joinType));
            corpRegs.remove(joinType);
        }

        for(RegInfo regInfo : corpRegs.values()){
            BusinessReg businessReg = new BusinessReg();
            businessReg.setId(new BusinessRegPK(regInfo,regBusiness));
            businessReg.setOperateType(BusinessReg.OperateType.QUOTED);
        }
        return regBusiness;
    }

    private CorpBusiness createCorp(CorpBusiness regBusiness){
        //TODO Check data


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
            reg.getId().setBusiness(regBusiness);
            reg.setOperateType(BusinessReg.OperateType.CREATE);
            reg.getId().getInfo().setId(defaultUidGenerator.getUID());
            reg.getId().getInfo().setPrevious(null);
        }
        return regBusiness;
    }





}
