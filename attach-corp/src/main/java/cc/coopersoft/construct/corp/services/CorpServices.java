package cc.coopersoft.construct.corp.services;


import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.data.GroupIdType;
import cc.coopersoft.common.data.OperationType;
import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.corp.model.*;
import cc.coopersoft.construct.corp.repository.CorpBusinessRepository;
import cc.coopersoft.construct.corp.repository.CorpRegRepository;
import cc.coopersoft.construct.corp.repository.CorpRepository;
import cc.coopersoft.construct.corp.repository.CreditRecordRepository;
import com.github.wujun234.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class CorpServices {

    private final static  int PAGE_SIZE = 20;

    private final static int NAMES_PAGE_SIZE = 10;

    private final CorpRepository corpRepository;

    private final CorpBusinessRepository corpBusinessRepository;

    private final CorpRegRepository corpRegRepository;

    private final RemoteServices remoteServices;

    private final CreditRecordRepository creditRecordRepository;


    @Resource
    private UidGenerator defaultUidGenerator;

    @Autowired
    public CorpServices(CorpRepository corpRepository,
                        CorpBusinessRepository corpBusinessRepository,
                        CorpRegRepository corpRegRepository,
                        RemoteServices remoteServices, CreditRecordRepository creditRecordRepository) {
        this.corpRepository = corpRepository;
        this.corpBusinessRepository = corpBusinessRepository;
        this.corpRegRepository = corpRegRepository;
        this.remoteServices = remoteServices;
        this.creditRecordRepository = creditRecordRepository;
    }

    public CreditRecord addCredit(long code, CreditRecord creditRecord){
        Corp corp = corpRepository.findById(code).orElseThrow();
        creditRecord.setCorp(corp);
        creditRecord.setRecordTime(new Date());
        creditRecord.setId(defaultUidGenerator.getUID());
        return creditRecordRepository.save(creditRecord);
    }

    public List<CreditRecord> credits(long code){
        return creditRecordRepository.findByCorpCodeOrderByRecordTimeDesc(code);
    }

    public Optional<CorpReg> corpReg(long corpCode, CorpProperty type){
       return corpRegRepository.findByIdCorpCodeAndIdProperty(corpCode,type);

    }

    public Optional<CorpBusiness> corpBusiness(long businessId){
        return this.corpBusinessRepository.findById(businessId);
    }

    public Optional<Corp> corp(long corpCode){
        return this.corpRepository.findById(corpCode);
    }

    public Optional<Corp> corp(GroupIdType type, String number){
       return corpRepository.findByInfoGroupIdTypeAndInfoGroupId(type,number);
    }


    public boolean existsCorpGroupNumber(GroupIdType type, String number){
       return corpRepository.existsByInfoGroupIdTypeAndInfoGroupId(type,number) ||
            corpBusinessRepository.existsByStatusAndInfoGroupIdTypeAndInfoGroupId(RegStatus.Running,type,number);
    }

    public boolean existsCorpGroupNumber(long code, GroupIdType type, String number){
        return corpRepository.existsByCodeNotAndInfoGroupIdTypeAndInfoGroupId(code,type,number) ||
                corpBusinessRepository.existsByCodeNotAndStatusAndInfoGroupIdTypeAndInfoGroupId(code,RegStatus.Running,type,number);
    }

    public boolean corpInBusiness(long corpCode){
        return this.corpBusinessRepository.existsByStatusAndCode(RegStatus.Running,corpCode);
    }

    public List<CorpBusiness> listBusiness(Long corpCode){
        return this.corpBusinessRepository.findByStatusInAndCodeOrderByCreateTime(
                EnumSet.of(RegStatus.Running,RegStatus.Register),corpCode);
    }

    public Page<Corp> names(Optional<String> key, int page){
        PageRequest pr = PageRequest.of(page,NAMES_PAGE_SIZE,Sort.Direction.DESC , "dataTime");

        if (key.isPresent() && StringUtils.isNotBlank(key.get().trim())){
            String _key = key.get().trim() + "%";
            log.debug("name search by:" + _key);
            return this.corpRepository.findByEnableIsTrueAndInfoNameLikeOrEnableIsTrueAndInfoGroupIdLike( "%" + _key, _key,pr );
        }else{
            log.debug("name search no key");
            return this.corpRepository.findByEnableIsTrue(pr);
        }

    }


    public Page<Corp> listAllCorp(Optional<Boolean> valid, Optional<CorpProperty> joinType,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){
        //TODO StringUtils.split(key)

        boolean validOnly = valid.orElse(false);

        Specification<Corp> specification = (Specification<Corp>) (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new LinkedList<>();



            Join<Corp,CorpInfo> infoJoin;

            if (criteriaQuery.getResultType().equals(Long.class)) {
                infoJoin = root.join("info", JoinType.INNER);
            }else{
                Fetch<Corp, CorpInfo> infoFetch = root.fetch("info", JoinType.LEFT);
                infoJoin =(Join<Corp, CorpInfo>)infoFetch;
            }
            if (key.isPresent() && StringUtils.isNotBlank(key.get())){
                List<Predicate> keyPredicate = new LinkedList<>();
                String _key = key.get().trim();
                String _keyLike = "%" + _key + "%";
                keyPredicate.add(cb.equal(root.get("code").as(String.class),_key));
                keyPredicate.add(cb.like(infoJoin.get("groupId").as(String.class), _keyLike ));
                keyPredicate.add(cb.like(infoJoin.get("ownerName").as(String.class), _keyLike));
                keyPredicate.add(cb.like(infoJoin.get("ownerId").as(String.class), _keyLike));
                keyPredicate.add(cb.like(infoJoin.get("address").as(String.class), _keyLike));
                keyPredicate.add(cb.like(infoJoin.get("tel").as(String.class),_keyLike));
                keyPredicate.add(cb.like(infoJoin.get("name").as(String.class),_keyLike));

                predicates.add(cb.or(keyPredicate.toArray(new Predicate[0])));

            }

            if (joinType.isPresent()){
                Join<Corp,CorpReg> regJoin = root.join("regs", JoinType.INNER);
                predicates.add(cb.and(cb.equal(regJoin.get("id").get("property").as(CorpProperty.class),joinType.get())));

            }

            if (validOnly){
                predicates.add(cb.and(cb.isTrue(root.get("enable").as(Boolean.class))));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sortable = Sort.by((dir.isPresent() ? ("DESC".equals(dir.get()) ? Sort.Direction.DESC : Sort.Direction.ASC) : Sort.Direction.DESC)
                , (sort.isPresent() ? sort.get() : "dataTime"));

        return corpRepository.findAll(specification, PageRequest.of(page.isPresent() ? page.get() : 0 ,PAGE_SIZE,sortable));
    }

    @Transactional
    public void setCorpEnable(long id, boolean enable){
        remoteServices.publishChangeMessage(id);
        corpRepository.findById(id).ifPresent(c -> {
            c.setEnable(enable);
            Corp result = corpRepository.save(c);
            remoteServices.publishUserChangeMessage(result.getEmployees().stream().map(CorpEmployee::getUsername).collect(Collectors.toList()));
        });

    }


    @Transactional()
    public Corp patchCreate(CorpBusiness business){
        CorpBusiness regBusiness = createCorp(business);

        // maybe in client
        regBusiness.setRegTime(new Date());
        regBusiness.setApplyTime(new Date());

        regBusiness.setSource(RegSource.Patch);
        regBusiness.setStatus(RegStatus.Register);

        Corp corp = new Corp();
        corp.setInfo(regBusiness.getInfo());
        corp.setCode(regBusiness.getCode());
        corp.setEnable(true);
        corp.setDataTime(new Date());

        String types = "";

        for(BusinessReg reg:  regBusiness.getRegs()){
            reg.getInfo().setRegTime(regBusiness.getRegTime());

            CorpReg corpReg = new CorpReg();
            corpReg.setId(new CorpRegPK(reg.getId().getProperty(),corp));
            corpReg.setInfo(reg.getInfo());
            corp.getRegs().add(corpReg);

            types = types + " " + reg.getId().getProperty().name();

            log.debug("add record type :" + reg.getId().getProperty().name());
        }

        corp.setTypes(types.trim());


        this.corpBusinessRepository.save(regBusiness);
        return this.corpRepository.save(corp);
    }

    @Transactional()
    public Corp patchModify(long corpCode, CorpBusiness business){
        remoteServices.publishChangeMessage(corpCode);
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
        // maybe client
        regBusiness.setRegTime(new Date());
        regBusiness.setApplyTime(new Date());
        regBusiness.setSource(RegSource.Patch);
        regBusiness.setStatus(RegStatus.Register);

        for(BusinessReg reg:  regBusiness.getRegs()){
            reg.getInfo().setRegTime(regBusiness.getRegTime());
        }

        regBusiness = this.corpBusinessRepository.saveAndFlush(regBusiness);

        corp.setDataTime(new Date());

        if (regBusiness.isInfoChanged()){
            corp.setInfo(regBusiness.getInfo());
        }

        Map<CorpProperty, CorpReg> corpRegs = new HashMap<>(corp.getRegs().size());

        for(CorpReg reg: corp.getRegs()){
            corpRegs.put(reg.getId().getProperty(),reg);
        }

        String types = "";

        for(BusinessReg reg:  regBusiness.getRegs()){
            switch (reg.getOperateType()){
                case DELETE:
                    corp.getRegs().remove(corpRegs.get(reg.getId().getProperty()));
                    log.debug("remove reg type: " + reg.getId().getProperty() + "; size is:" + corp.getRegs().size());
                    break;
                case MODIFY:
                    corp.getRegs().remove(corpRegs.get(reg.getId().getProperty()));
                case CREATE:

                    CorpReg corpReg = new CorpReg();
                    corpReg.setId(new CorpRegPK(reg.getId().getProperty(),corp));
                    corpReg.setInfo(reg.getInfo());
                    corp.getRegs().add(corpReg);
                    break;
            }
            if (!OperationType.DELETE.equals(reg.getOperateType())){
                types = types + " " + reg.getId().getProperty().name();
            }
        }
        corp.setTypes(types.trim());

        return this.corpRepository.save(corp);
    }



    private CorpBusiness modifyCorp(Corp corp, CorpBusiness regBusiness){

        regBusiness.setCreateTime(new Date());
        regBusiness.setId(defaultUidGenerator.getUID());
        regBusiness.setCode(corp.getCode());

        if (regBusiness.getInfo() != null){
            if (existsCorpGroupNumber(corp.getCode(),
                    regBusiness.getInfo().getGroupIdType(),
                    regBusiness.getInfo().getGroupId())){
                throw new IllegalArgumentException("企业证件编号已经被占用");
            }


            regBusiness.setInfoChanged(true);
            regBusiness.setCode(corp.getCode());
            regBusiness.getInfo().setPrevious(corp.getInfo());
            regBusiness.getInfo().setId(defaultUidGenerator.getUID());
        }else{
            regBusiness.setInfoChanged(false);
            regBusiness.setInfo(corp.getInfo());
        }

        Map<CorpProperty, CorpReg> corpRegs = new HashMap<>(corp.getRegs().size());

        for(CorpReg reg: corp.getRegs()){
            corpRegs.put(reg.getId().getProperty(),reg);
        }

        for(BusinessReg reg:  regBusiness.getRegs()) {
            CorpProperty joinType = reg.getId().getProperty();
            switch (reg.getOperateType()){
                case QUOTED:
                    throw new IllegalArgumentException("操作类型错误：" +  reg.getOperateType());
                case DELETE:
                    reg.setInfo(corpRegs.get(joinType).getInfo());
                    break;
                case MODIFY:
                    log.debug("add modify info level number:" + reg.getInfo().getLevelNumber());
                    reg.getInfo().setPrevious(corpRegs.get(joinType).getInfo());
                case CREATE:
                    reg.getInfo().setId(defaultUidGenerator.getUID());
                    break;
            }
            reg.getId().setBusiness(regBusiness);
            corpRegs.remove(joinType);
        }

        for(CorpReg corpReg : corpRegs.values()){
            BusinessReg businessReg = new BusinessReg(regBusiness,corpReg.getId().getProperty());
            businessReg.setOperateType(OperationType.QUOTED);
            businessReg.setInfo(corpReg.getInfo());
            regBusiness.getRegs().add(businessReg);
        }
        return regBusiness;
    }

    private CorpBusiness createCorp(CorpBusiness regBusiness){

        if (existsCorpGroupNumber(regBusiness.getInfo().getGroupIdType(),
                regBusiness.getInfo().getGroupId())){
            throw new IllegalArgumentException("企业证件编号已经存在!" );
        }

        regBusiness.setCreateTime(new Date());
        regBusiness.setId(defaultUidGenerator.getUID());
        regBusiness.setInfoChanged(true);


        regBusiness.getInfo().setId(defaultUidGenerator.getUID());
        regBusiness.setCode(defaultUidGenerator.getUID());
        regBusiness.getInfo().setPrevious(null);

        for(BusinessReg reg:  regBusiness.getRegs()){
            reg.getInfo().setId(defaultUidGenerator.getUID());
            reg.getId().setBusiness(regBusiness);
            reg.setOperateType(OperationType.CREATE);
            reg.getInfo().setPrevious(null);
        }
        return regBusiness;
    }





}
