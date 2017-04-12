--------------------------------------------------------
--  文件已创建 - 星期三-四月-12-2017
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table MWM_ACCOUNT_SECURITY
--------------------------------------------------------

  CREATE TABLE "LOYAL"."MWM_ACCOUNT_SECURITY"
   (	"M_ACCOUNT" VARCHAR2(18 BYTE),
	"M_TIME" DATE,
	"M_DEVICE" VARCHAR2(100 BYTE),
	"M_MAC" VARCHAR2(18 BYTE)
   ) SEGMENT CREATION IMMEDIATE
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 8192 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into LOYAL.MWM_ACCOUNT_SECURITY
SET DEFINE OFF;
Insert into LOYAL.MWM_ACCOUNT_SECURITY (M_ACCOUNT,M_TIME,M_DEVICE,M_MAC) values ('admin',to_date('16-3月 -17','DD-MON-RR'),null,null);
Insert into LOYAL.MWM_ACCOUNT_SECURITY (M_ACCOUNT,M_TIME,M_DEVICE,M_MAC) values ('l6yang',to_date('29-3月 -17','DD-MON-RR'),'Xiaomi(MI PAD)','2A:3A:D5:C2:DB:8A');
--------------------------------------------------------
--  Constraints for Table MWM_ACCOUNT_SECURITY
--------------------------------------------------------

  ALTER TABLE "LOYAL"."MWM_ACCOUNT_SECURITY" MODIFY ("M_ACCOUNT" NOT NULL ENABLE);

  ALTER TABLE "LOYAL"."MWM_ACCOUNT_SECURITY" MODIFY ("M_TIME" NOT NULL ENABLE);
