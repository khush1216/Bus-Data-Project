LOAD DATA LOCAL INFILE 'C:/Users/Khushbu/Desktop/uic/main/DBMS/civisAnalyticsDemo/CTA_data.csv' INTO TABLE ridesharing.cta_dataset
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(stop_id,on_street,cross_street,routes,boardings,alightings,month_beginning,daytype,location);