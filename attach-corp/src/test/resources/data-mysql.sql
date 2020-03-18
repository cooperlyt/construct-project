
USE CONSTRUCT;

# insert into CORP_INFO (ADDRESS, CORP_CODE, REG_ID_NUMBER, REG_ID_TYPE, NAME, OWNER_ID_NUMBER, OWNER_ID_TYPE, OWNER_NAME, PREVIOUS, TEL, id)
#     values ('?', 'test-modify', 'test-modify', 'COMPANY_CODE', 'test modify', '?', 'MASTER_ID', 'test', null, 'ddd', 0);
#
#
# insert into CORP_REG_BUSINESS (CORP_INFO, CREATE_TIME, INFO, REG_DATE, SOURCE, STATUS, TAGS, BUSINESS_ID)
# values (0, now(), true, now(), 'OLD', 'valid', '', 0);
# insert into BUSINESS_REG (OPERATE, BUSINESS_ID, REG_ID) values ('CREATE', 0 , 0);
# insert into CONSTRUCT_CORP (DATA_TIME, ENABLE, CORP_INFO, TYPES, CORP_CODE) values (?, ?, ?, ?, ?)
# insert into CONSTRUCT_CORP_REG (REG_ID, CORP_CODE, CORP_TYPE) values (?, ?, ?)

# SET SESSION FOREIGN_KEY_CHECKS=0;
#
# insert into CORP_INFO (ADDRESS, CORP_CODE, REG_ID_NUMBER, REG_ID_TYPE, NAME, OWNER_ID_NUMBER, OWNER_ID_TYPE, OWNER_NAME, PREVIOUS, TEL, ID)
# values ('?', 'test-modify', 'test-modify', 'COMPANY_CODE', 'test modify', '?', 'MASTER_ID', 'test', null, 'ddd', 1);
# insert into CORP_REG_BUSINESS (CORP_INFO, CREATE_TIME, INFO, REG_DATE, SOURCE, STATUS, TAGS, BUSINESS_ID)
# values (1, now(), true, '2020-1-1', 'OLD', 'valid', '', 1);
# insert into CONSTRUCT_CORP_REG_INFO (LEVEL, LEVEL_NUMBER, PREVIOUS, REG_DATE_TO, CORP_TYPE, ID)
# values (1, '11', null, now(), 'Developer', 1);
# insert into BUSINESS_REG (BUSINESS_ID, REG_ID, OPERATE, ID)
# values (1, 1, 'CREATE', 1);
# insert into CONSTRUCT_CORP (DATA_TIME, ENABLE, CORP_INFO, TYPES, CORP_CODE)
# values (now(), true, 1, 'Developer', 'test-modify');
# insert into CONSTRUCT_CORP_REG (REG_ID, CORP_CODE, CORP_TYPE)
# values (1, 'test-modify', 'Developer');
#
# SET SESSION FOREIGN_KEY_CHECKS=1;