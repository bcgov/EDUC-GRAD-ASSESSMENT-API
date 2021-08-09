-- ASSESSMENT_CODE definition

  CREATE TABLE "ASSESSMENT_CODE" 
   (	"ASSESSMENT_CODE" VARCHAR2(7 BYTE), 
	"LABEL" VARCHAR2(65 BYTE),
	"DESCRIPTION" VARCHAR2(255 BYTE),
	"DISPLAY_ORDER" NUMBER NOT NULL ENABLE,
	"ASSESSMENT_LANGUAGE" VARCHAR2(3 BYTE),
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE,
	"EXPIRY_DATE" DATE,
	"CREATE_USER" VARCHAR2(32 BYTE) DEFAULT USER NOT NULL ENABLE,
	"CREATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
	"UPDATE_USER" VARCHAR2(32 BYTE) DEFAULT USER NOT NULL ENABLE,
	"UPDATE_DATE" DATE DEFAULT SYSTIMESTAMP NOT NULL ENABLE, 
	 CONSTRAINT "ASSESSMENT_CODE_PK" PRIMARY KEY ("ASSESSMENT_CODE")
  USING INDEX TABLESPACE "API_GRAD_IDX"  ENABLE
   ) SEGMENT CREATION IMMEDIATE 
 NOCOMPRESS LOGGING
  TABLESPACE "API_GRAD_DATA" NO INMEMORY ;
  