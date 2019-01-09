INSERT INTO TEST (
    ID,
    VERSION,
    CREATED_BY,
    CREATED_DATE,
    VAR_BYTE, 
    VAR_CH, 
    VAR_SHORT, 
    VAR_INT, 
    VAR_LONG,
    VAR_BIG_INTEGER,
    VAR_DOUBLE,
    VAR_BIG_DECIMAL,
    VAR_BOOL,
    VAR_DATE,
    VAR_TIME,
    VAR_DTTM)
VALUES (  
    76, 
    0,
    'sysgen',
    '2018-10-01T12:00:00',
    convert('ae', binary(8)), 
    'A', 
    32767, 
    2147483647, 
    9223372036854775807,
    2147483647,
    9999999999999999.99,
    9999999999999999.9999,
    TRUE,
    '2018-09-01',
    '10:15:30',
    '2015-03-25T12:00:00');

INSERT INTO USER_PROFILE (
    ID,
    VERSION,
    CREATED_BY,
    CREATED_DATE,
    FIRST_NAME, 
    LAST_NAME, 
    EMAIL)
VALUES (  
    75, 
    0,
    'sysgen',
    '2018-10-01T12:00:00',
    'Clifff', 
    'Dunnn', 
    'clifffdunnntx@yahoo.com');

