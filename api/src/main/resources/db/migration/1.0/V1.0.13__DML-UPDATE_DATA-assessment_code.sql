UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2017-10-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), EXPIRY_DATE = TO_DATE('2019-06-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
WHERE ASSESSMENT_CODE = 'NME';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2017-10-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS'), EXPIRY_DATE = TO_DATE('2019-06-30 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS')
WHERE ASSESSMENT_CODE = 'NMF';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2019-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'LTE10';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2019-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'LTP10';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2019-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'NME10';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2019-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'NMF10';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2021-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'LTE12';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2021-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'LTF12';
UPDATE ASSESSMENT_CODE SET EFFECTIVE_DATE = TO_DATE('2021-07-01 00:00:00.000', 'YYYY-MM-DD HH24:MI:SS') WHERE ASSESSMENT_CODE = 'LTP12';
