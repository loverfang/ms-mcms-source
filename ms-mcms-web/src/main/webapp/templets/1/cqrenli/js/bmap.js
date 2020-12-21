function initMap(cd,company,phone,address){
		
		var temp = cd.split(',');

	    var map = new BMap.Map("container"); 
	    
	    var point = new BMap.Point(temp[0],temp[1]);
	    
	    map.centerAndZoom(point, 15);
	    
	    var opts = {type:BMAP_NAVIGATION_CONTROL_LARGE};  
		
		map.addControl(new BMap.NavigationControl(opts));
           
  		var marker=new BMap.Marker(point);
  			
  		map.addOverlay(marker);  
  		
  		var licontent = "<h3 style=' font-family:微软雅黑; font-size:14px; color:#333; border-bottom:1px #CCCCCC solid; height:30px; line-height:30px;margin:0; padding-left:12px;'>"+company+"</h3>"
        licontent+="<p style='font-family:微软雅黑;font-size:12px; color:#666; height:24px; line-height:26px; display: block;margin:0;border-bottom:1px #CCCCCC dashed; background:#fcfcfc;padding-left:12px;'>电话："+phone+"</p>";
        licontent+="<p style='font-family:微软雅黑;font-size:12px; color:#666; height:24px; line-height:26px; display: block;margin:0;border-bottom:1px #CCCCCC dashed;padding-left:12px; '> 地址："+address+"</p>";
 
     	
     	var opts1 = { width: 300,height:100 };
  
    	var infoWindow = new BMap.InfoWindow(licontent, opts1);
			  
		marker.openInfoWindow(infoWindow); 
 }