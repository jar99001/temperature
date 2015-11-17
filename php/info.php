<?php
	mysql_connect("localhost","nisse","rumpa");
	mysql_select_db("temperatur");
	
	//$q=mysql_query("select * from temp where (datum between date_sub(now(),INTERVAL 1 ".$_REQUEST['intervall'].") and now())");
	
	switch($_REQUEST['intervall']) {
		case "DAY":
			$q = mysql_query("select * from temp where (datum between date_sub(now(),INTERVAL 1 DAY) and now())");
		break;
		case "WEEK":
			$q = mysql_query("select * from temp where (datum between date_sub(now(),INTERVAL 1 WEEK) and now()) and (klockslag='12:00:00' or klockslag='00:00:00' or klockslag='06:00:00' or klockslag='18:00:00')");
		break;
		case "MONTH":
			$q = mysql_query("select * from temp where (datum between date_sub(now(),INTERVAL 1 MONTH) and now()) and klockslag='18:00:00'");
		break;
		case "YEAR":
			$q = mysql_query("select * from temp where (datum between date_sub(now(),INTERVAL 1 YEAR) and now()) and (DAYOFWEEK(datum)=1 and klockslag='18:00:00')");
		break;
	} 
	
	// Every value the last day
	// select * from temp where (datum between date_sub(now(),INTERVAL 1 DAY) and now());
	
	// Four values for every day for the last week
	//select * from temp where (datum between date_sub(now(),INTERVAL 1 WEEK) and now()) and (klockslag='12:00:00' or klockslag='00:00:00' or klockslag='06:00:00' or klockslag='18:00:00');
	
	// One value for every day for the last month
	//select * from temp where (datum between date_sub(now(),INTERVAL 1 MONTH) and now()) and klockslag='18:00:00';
	
	// Every sunday 18:00:00 a'clock for the whole year
	//select * from temp where (datum between date_sub(now(),INTERVAL 1 YEAR) and now()) and (DAYOFWEEK(datum)=1 and klockslag='18:00:00');
	
	while($e = mysql_fetch_assoc($q)) $output[] = $e;
	
	print(json_encode($output));
	 
	mysql_close();
?>