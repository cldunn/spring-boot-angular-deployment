INSERT INTO TEST (ID, CREATED_BY, CREATED_DATE, MAINTAINED_BY, MAINTAINED_DATE, VERSION, 
                  VAR_CH, VAR_SHORT, VAR_INT, VAR_LONG, VAR_BIG_INTEGER, VAR_DOUBLE, VAR_BIG_DECIMAL, VAR_BOOL, VAR_DATE, VAR_TIME, VAR_DTTM)
VALUES (2, 'sysgen', getDate(), null, null, 0, 'B', 10, 100, 1000, 10000, 420.12, 240.78, 0, '2016-08-03', '06:45:05', '2018-10-01 07:15:00');

INSERT INTO TEST (ID, CREATED_BY, CREATED_DATE, MAINTAINED_BY, MAINTAINED_DATE, VERSION, 
                  VAR_CH, VAR_SHORT, VAR_INT, VAR_LONG, VAR_BIG_INTEGER, VAR_DOUBLE, VAR_BIG_DECIMAL, VAR_BOOL, VAR_DATE, VAR_TIME, VAR_DTTM)
VALUES (3, 'sysgen', getDate(), null, null, 0, 'C', 11, 101, 1001, 10001, 420.13, 240.79, 1, '2016-08-04', '06:46:00', '2018-11-01 07:15:00');

INSERT INTO TEST (ID, CREATED_BY, CREATED_DATE, MAINTAINED_BY, MAINTAINED_DATE, VERSION,
		          VAR_CH, VAR_SHORT, VAR_INT, VAR_LONG, VAR_BIG_INTEGER, VAR_DOUBLE, VAR_BIG_DECIMAL, VAR_BOOL, VAR_DATE, VAR_TIME, VAR_DTTM)
VALUES (76, 'sysgen', '2018-10-01T12:00:00', null, null,  0,
        'A', 32767, 2147483647, 9223372036854775807, 2147483647, 
        9999999999999999.99, 9999999999999999.9999, 1, '2018-09-01', '10:15:30', '2015-03-25T12:00:00');
