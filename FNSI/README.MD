1. Cсылка для выгрузки json паспорта справочника
по его версии и идентификатору: 
1.1. Образец: https://nsi.rosminzdrav.ru/port/rest/passport?userKey=key&identifier=system&version=version
1.2. Рабочий вариант: https://nsi.rosminzdrav.ru/port/rest/passport?userKey=2b6a3146-9b41-4d0a-a3b0-51d294cf2e03&identifier=1.2.643.5.1.13.13.11.1002&version=9.3

2. Ссылка для выгрузки json справочника по версии, идентификатору и коду:
2.1 Образец https://nsi.rosminzdrav.ru/port/rest/data?userKey=key&identifier=system&version=version&columns=code,display&filters=code|value
2.2 Рабочий вариант https://nsi.rosminzdrav.ru/port/rest/data?userKey=2b6a3146-9b41-4d0a-a3b0-51d294cf2e03&identifier=1.2.643.5.1.13.13.11.1005&version=2.23&columns=MKB_CODE,MKB_NAME&filters=MKB_CODE|K35.3
3. Ссылка для выгрузки json справочника по версии, идентификатору,сортировке:
3.1 Образец https://nsi.rosminzdrav.ru/port/rest/data?userKey=Key&identifier=system&version=version&page=page&size=size&columns=code,display&sorting=display
3.2 Рабочий вариант https://nsi.rosminzdrav.ru/port/rest/data?userKey=2b6a3146-9b41-4d0a-a3b0-51d294cf2e03&identifier=1.2.643.5.1.13.13.11.1005&version=2.23&page=1&size=100&columns=MKB_CODE,MKB_NAME&sorting=MKB_NAME
