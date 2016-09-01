var IE7 = false;	
var root = "http://" + document.location.hostname;
var cPage = "";

init();

function init(){
	if (navigator.appVersion.indexOf("Mac")!=-1) {
		jQuery('body').addClass('mac');
	} else {
		jQuery('body').addClass('realpc');
	}	
	if (navigator.appVersion.indexOf("MSIE 7")!=-1) {
		IE7 = true;	
	}
}

function showNav(){
	jQuery("#globalNav").animate({"margin-top":"0px"},300);
	jQuery("#showNav").fadeOut(200);
}
function closeNav(){
	jQuery("#globalNav").animate({"margin-top":"-60px"},300);
	jQuery("#showNav").fadeIn(200);
}

jQuery("#collections").click(function(e){
	e.preventDefault();
	showCatSelect();
});

function showCatSelect(){
	jQuery("#catSelect").fadeIn();
}
function closeCatSelect(){
	jQuery("#catSelect").fadeOut();
}

/*************************     BRANDBOX SLIDER   *******************************/


var cP = 1;
if(jQuery('#brandbox').length){
	var tP = jQuery('#sliderContent').find("img").length;
}


function gotoPanel(id){
	if(id != cP){
		jQuery('#sliderContent').animate({left: -(795 * (id-1))}, 400);		
		
		document.getElementById("b" + id).className = "a";
		document.getElementById("b" + cP).className = "";
		
		cP = parseInt(id);
		if(cP == 1){			
			document.getElementById("bLeft").className += " d";	
			document.getElementById("bRight").className = "bNav";
		}else if(cP == tP){			
			document.getElementById("bLeft").className = "bNav";
			document.getElementById("bRight").className += " d";	
		}else{			
			document.getElementById("bLeft").className = "bNav";
			document.getElementById("bRight").className = "bNav";
		}
		
		Cufon.refresh();
	}	
}


function bL(){
	if(cP != 1){
		id = cP-1;
		gotoPanel(id);
		
	}
}
function bR(){
	if(cP != tP){
		id = cP+1;
		gotoPanel(id);
	}
}

/*************************   ABOUT SLIDER   *******************************/


if(document.getElementById("tP")){
	var cP = 1;
	var tP = document.getElementById("tP").value;
	
	function mD(id){
		if(id != cP){
			jQuery('#detailHolder').animate({left: -(790 * (id-1))}, 400);		
			jQuery('#history').animate({left: -(376 * (id-1))}, 400);		
			
			if(IE7 == false){
				jQuery('#d' + id).animate({opacity: 1}, 250);	
				jQuery('#d' + cP).animate({opacity: 0}, 250);	
			}
			
			document.getElementById("h" + id).className = "a";
			document.getElementById("h" + cP).className = "";
					
			cP = id;
			if(cP == 1){			
				document.getElementById("mLeft").className += " d";	
				document.getElementById("mRight").className = "mNav";
			}else if(cP == tP){			
				document.getElementById("mLeft").className = "mNav";
				document.getElementById("mRight").className += " d";	
			}else{			
				document.getElementById("mLeft").className = "mNav";
				document.getElementById("mRight").className = "mNav";
			}		
			
			Cufon.refresh();
		}	
	}
	function mL(){
		if(cP != 1){
			id = cP-1;
			mD(id);
			
		}
	}
	function mR(){
		if(cP != tP){
			id = cP+1;
			mD(id);
		}
	}
}

/*********************     COLLECTION SLIDER   *******************************/

if(jQuery("#collectionTop").length){
	var cc = jQuery("#collectionTop h1").attr("id");
	cc = parseFloat(cc.substring(4, 10));
	mc = jQuery("#collectionTop").find("a").length + 1;	
	
	if(cc == 1){
		pc = mc;
	}else{
		pc = cc - 1;
	}
	
	if(cc == mc){
		nc = 1;
	}else{
		nc = cc + 1;
	}
	
	jQuery("#cat_" + pc).addClass("prev");
	jQuery("#cat_" + nc).addClass("next");
}

if(document.getElementById("slider")){
	
	var cC = cCI();    /* COLLECTION COUNT */
	var fP = 1;    /* FIRST PRODUCT */
	var a = false; /* IS ANIMATING ? */
	
}

function sR(){ /* SLIDE RIGHT */
	if(a == false){			
		if(fP != cC-2 && cC > 3){			
			a = true;
			if(cC - (fP+2) >= 3){
				s = 3;	
			}else{
				s = cC - (fP+2);
			}
			for(i = 1; i <= cC;i++){
				mL = parseInt(gS(document.getElementById('p' + i), "left"));	
				pW = parseInt(gS(document.getElementById('p' + i), "width"));	
				jQuery('#p' + i).delay((100*(i-1)) - ((fP-3)*100)).animate({left: mL - (pW*s)}, 100*(s+2));
			}
			var t = setTimeout ( function(){a = false;}, (100*(cC-1))+200 );
			fP = fP + s;			
			if(fP == cC-2){
				document.getElementById("slideRight").className += " d";				
			}
			document.getElementById("slideLeft").className = "slideNav";
			
		}
	}
}
function sL(){ /* SLIDE LEFT */
	if(a == false){
		if(fP != 1){
			a = true;
			if((fP-1)%3 != 0){
				if(fP >3){
					if(fP%2 == 0){
						s = 2;
					}else{
						s = 1;
					}
				}else{
					s = fP -1;
				}
			}else{
				s = 3;				
			}	
			//alert("fP: " + fP + "   ;   s: " + s);
			for(i = 1; i <= cC;i++){
				mL = parseInt(gS(document.getElementById('p' + i), "left"));	
				pW = parseInt(gS(document.getElementById('p' + i), "width"));	
				jQuery('#p' + i).delay(100*(fP-i+2)).animate({left: mL + (pW*s)}, 100*(s+2));	
			}
			var t = setTimeout ( function(){a = false;}, (100*(cC-1))+200 );
			fP = fP - s;
			if(fP == 1){
				document.getElementById("slideLeft").className += " d";
			}
			document.getElementById("slideRight").className = "slideNav";
			
		}
	}
}

function cCI(){  /* COUNT COLLECTION ITEMS */
	cC = 0;
	arr = document.getElementById("slider").childNodes;	
	for(var i=0;i<arr.length;i++){
		if(arr[i] != "[object Text]"){cC++;}
	}	
	return(cC);
}


/*********************   PRODUCT DETAIL FUNCTIONS  *******************************/

if(document.getElementById("cP")){
	prevId = 0;
	showPD(document.getElementById("cP").value);
	prevId = document.getElementById("cP").value; /* ID OF CURRENTLY DISPLAYED PRODUCT */
	
	first = true;
	
	if (window.addEventListener){			
		window.addEventListener('load', sN, false);
		window.addEventListener('resize', sN, false);	
	}else{
		window.onload = document.onload =  sN;
		window.onresize = document.onresize = sN;
	}
}

function sN(){  /* SHOW/HIDE NAV */
	if(navigator.appName == "Microsoft Internet Explorer"){
		var v = navigator.appVersion;
		ioH = v.indexOf("(");
		v = v.substr(0, ioH - 1);	
		if(v == "5.0"){
			wW = window.innerWidth;
		}else{
			wW = document.body.clientWidth;
		}
	}else{
		wW = window.innerWidth;
	}
	if(parseInt(wW) <= 1100){
		if(first == true){
			first = false;
			jQuery('#minNav').show();
			jQuery('#maxNav').hide();	
		}else{
			jQuery('#minNav').fadeIn();
			jQuery('#maxNav').fadeOut();
		}
	}else{
		if(first == true){
			first = false;
			jQuery('#minNav').hide();
			jQuery('#maxNav').show();	
		}else{
			jQuery('#maxNav').fadeIn();
			jQuery('#minNav').fadeOut();
		}
	}
	
}

function showP(id){  /* SCROLL TO PRODUCT DETAIL */
	jQuery('html,body').animate({scrollTop: 870}, 450);
	showPD(id);	
}

function showPD(id){ /* SHOW PRODUCT DETAIL */
	if(prevId != id){
		jQuery('#pD' +prevId).fadeOut();
		jQuery('#pD' +id).fadeIn();
		prevId = parseInt(id);
		
		if(prevId == 1){
			document.getElementById("goLeft").className += " d";
			document.getElementById("goRight").className = "goNav";
			document.getElementById("gL").className += " d";
			document.getElementById("gR").className = "goNav";
		}else if(prevId == cC){
			document.getElementById("goLeft").className = "goNav";
			document.getElementById("goRight").className += " d";
			document.getElementById("gL").className = "goNav";
			document.getElementById("gR").className += " d";
		}else{
			document.getElementById("goLeft").className = "goNav";
			document.getElementById("goRight").className = "goNav";
			document.getElementById("gL").className = "goNav";
			document.getElementById("gR").className = "goNav";
		}
	}
}

function b(){	/*  BACK TO COLLECTIONS  */
	jQuery('html,body').animate({scrollTop: 100}, 300);
}

function c(){	/*  BACK TO COLLECTIONS  */
	jQuery('html,body').animate({scrollTop: 455}, 300);
}


function gL(){  /*  NAVIGATE BETWEEN PRODUCTS (  go left ) */
	if(prevId != 1){
		showPD(parseInt(prevId) - 1);	
	}
}
function gR(){ /*  NAVIGATE BETWEEN PRODUCTS (  go right ) */	
	if(prevId != cC){
		showPD(parseInt(prevId) + 1);	
	}
}


/*********************   NEWS  *******************************/

function t(){ /* SCROLL TO TOP */
	jQuery('html,body').animate({scrollTop: 0}, 300);
}

/*************************   DISTRIBUTION  ***********************************/


jQuery(window).load(function() {
	if(document.getElementById("map")){
		var u = window.location.href + "";
		
		u = u.split("/");
		l = u.length;
		
		
		if(l <= 6){
			if (navigator.geolocation)
		    {	    	
		    	navigator.geolocation.getCurrentPosition(function(position){	    	
			    	uLat = position.coords.latitude;
			    	uLon = position.coords.longitude;
			    	
			    	var myLatlng = new google.maps.LatLng(uLat,uLon);
			    	
			    	 map.setCenter(myLatlng); 
			    	 map.setZoom(11);
		    	});
		    }
	    }
		
		
		var zoom = 2;
		var cLat = 40;
		var cLon = 0;
		
		if(l >= 7){
			zoom = 8;
			cLat = jQuery("#clat").val()
			cLon = jQuery("#clon").val()
		}
		if(l >= 8 && u[l-1] != ""){
			zoom = 11;
			cLat = jQuery("#search_lat").val()
			cLon = jQuery("#search_lon").val()
			
			jQuery("#searchField").val(urldecode(u[l-1]));			
		}
		
		var searchString = jQuery("#searchField").val();
		if(searchString){	
			for(i=0;i<l-1;i++){
				cPage += u[i] + "/";
			}
		}else{
			for(i=0;i<l;i++){
				cPage += u[i] + "/";
			}
		}
		
		var styles_shops = [{
			url: root + "/images/layout/icon-multiple.png",
	        height: 50,
	        width: 34,
	        anchor: [10, 0],
	        textColor: '#fff',
	        textSize: 11
	    }];
	    
	    var styles_dealers = [{
			url: root + "/images/layout/icon-multiple-dealers.png",
	        height: 50,
	        width: 34,
	        anchor: [10, 0],
	        textColor: '#000',
	        textSize: 11
	    }];

		var myOptions = {
			zoom: zoom,
			center:  new google.maps.LatLng(cLat,cLon),
			mapTypeId: google.maps.MapTypeId.ROADMAP,
			mapTypeControl: false,
			mapTypeControlOptions: {
				style: google.maps.MapTypeControlStyle.DEFAULT					 
			},
			scaleControl: false,
			streetViewControl: false,
			navigationControl: true,
			navigationControlOptions: {
				style: google.maps.NavigationControlStyle.SMALL		
			}
		}		
		
		var map = new google.maps.Map(document.getElementById("map"), myOptions);
		
		google.maps.event.addListener(map, 'click', function() {
			infowindow.close();
		});
		
		
		
		var infowindow = new google.maps.InfoWindow(
		{ 
			size: new google.maps.Size(500,160)
		});
		
		var markers_shops = [];
		var markers_dealers = [];
		
		
		jQuery("#mapData").find("div").each(function(){
			var id = jQuery(this).attr("id");
			var type = jQuery(this).attr("class");
			var title = jQuery("#title_" + id).val()
			var lat = jQuery("#lat_" + id).val()
			var lon = jQuery("#lon_" + id).val()
			var cat = jQuery("#cat_" + id).val()
			
			var adres    = jQuery("#adres_" + id).val()
			var postcode = jQuery("#postcode_" + id).val()
			var city     = jQuery("#city_" + id).val()
			var tel      = jQuery("#tel_" + id).val()
			
			var html = "<div class='infoWindow'><h3>" + title + "</h3><p>" + adres + "<br/>" + postcode + " " + city + "<br/>T: " + tel + "</p></div>";
			
			var point = new google.maps.LatLng(lat,lon);
			if(type == "Dealer"){
				var icon = root + "/images/layout/icon-dealer.png";
			}
			else{
				var icon = root + "/images/layout/icon-shop.png";
			}
			var marker = createMarker(map,point,title,id,icon,cat,html,infowindow);
			
			if(type != "Dealer"){
				markers_shops.push(marker);
			}else{
				markers_dealers.push(marker);
			}
			
		});
		
		
		var mc_shops = new MarkerClusterer(map, markers_shops,{styles: styles_shops});
		var mc_dealers = new MarkerClusterer(map, markers_dealers,{styles: styles_dealers});
	}
});


function createMarker(map,latlng, name, id,icon,cat,html,infowindow) {
	var contentString = html;
	var marker = new google.maps.Marker({
		position: latlng,
		map: map,
		icon: icon
	});
	google.maps.event.addListener(marker, 'click', function() {
		var u = window.location.href + "";
		u = u.split("/");
		u = u.length;
		
		if(u == 5){
			window.location += "/c/" + cat;
		}
		if(u == 6){
			window.location = "../c/" + cat;
		}
		if(u >= 7){			
			infowindow.setContent(contentString); 
			infowindow.open(map,marker);
		}
	});
	return marker;
}


function sC(elem){
    var selectedVal = jQuery('ul.ddOptionList').find('a.selected').text();
	if(selectedVal != "Select a country"){
		changeCountry(selectedVal.toLowerCase());
	}
    jQuery('#value').html(selectedVal);
}


prevID = 1;

function doNav(id){
	if(id != prevID){
		document.getElementById("block" + prevID).style.display = "none";
		document.getElementById("block" + id).style.display = "block";
		
		document.getElementById("n" + prevID).className = "";
		document.getElementById("n" + id).className = "active";
		prevID = id;
	}
}

function doSearch(){
	var searchString = jQuery("#searchField").val();
	if(searchString){
		searchString = urlencode(searchString);
		window.location = cPage + searchString;
	}else{
		window.location = cPage;
	}
}

/*************************   DOWNLOADS  ***********************************/

function o(id){	
	gH = parseInt(gS(document.getElementById(id), "height"));
	if(gH == 0){
		 doo(id)
	}else{
		 doc(id)
	}
}
function doo(id){
	h = document.getElementById("c" + id).value;
	document.getElementById("a" + id).className = "active cat";
	jQuery('#' + id).animate({height: h * 30}, 300);
}
function doc(id){
	document.getElementById("a" + id).className = "cat";
	jQuery('#' + id).animate({height: 0}, 300);
}

var $idown;  // Keep it outside of the function, so it's initialized once.

function downloadURL(url) {
  if ($idown) {
    $idown.attr('src',url);
  } else {
    $idown = jQuery('<iframe>', { id:'idown', src:url }).hide().appendTo('body');
  }
}

function downloadManual(id){
	window.open(jQuery("#downloads" + id).val());
}


/*********************   GENERAL FUNCTIONS  *******************************/

/*
function urlencode (str) {    
    str = (str+'').toString();
    return encodeURIComponent(str).replace(/%20/g, '+').replace(/,/g, '%2C');
}
*/
function urlencode (str) {    
    str = (str+'').toString();
    return encodeURIComponent(str);
}
function urldecode (str) {    
    str = (str+'').toString();
    return decodeURIComponent(str);
}

function gS(oElm, strCssRule){  /* GETSTYLE */
	var strValue = "";
	if(document.defaultView && document.defaultView.getComputedStyle){
		strValue = document.defaultView.getComputedStyle(oElm, "").getPropertyValue(strCssRule);
	}
	else if(oElm.currentStyle){	
		strCssRule = strCssRule.replace(/\-(\w)/g, function (strMatch, p1){
			return p1.toUpperCase();
		});
		strValue = oElm.currentStyle[strCssRule];
	}
	if(strValue == "auto" || strValue == "100%"){
		if(strCssRule == "width"){
			strValue = oElm.offsetWidth;	
		}
		if(strCssRule == "height"){
			strValue = oElm.offsetHeight;
		}
	}
	return strValue;
}

function p(u){	 /* CREATE POP-UP */
    w = window.open("http://www.facebook.com/sharer.php?u=" + u, "Share on Facebook", "location=0,status=0,scrollbars=0, width=575,height=380");    
}

function eC(needle) {
	var my_array = document.getElementsByTagName("*");
	var retvalue = new Array();
	var i;
	var j;
	for (i=0,j=0;i<my_array.length;i++) {
		var c = " " + my_array[i].className + " ";
		if (c.indexOf(" " + needle + " ") != -1) retvalue[j++] = my_array[i];
	}
	return retvalue;
}


/*********************   CATSELECT FUNCTIONS  *******************************/

jQuery(window).load(function() {
	jQuery("a.hasSub").click(function(){
		checkOpenSiblings(jQuery(this))
		toggleMenu(jQuery(this));
	});
});


function toggleMenu(obj){
	obj.toggleClass("isOpen");
	obj.parent().find("ul:first").toggleClass("open");
}

function checkOpenSiblings(obj){
	if(obj.hasClass("firstLevel")){
		if(obj.html() != jQuery("#catHolder").find(".firstLevel.isOpen").html()){
			toggleMenu(jQuery("#catHolder").find(".firstLevel.isOpen"));
		}
	}
	if(obj.hasClass("secondLevel")){
		if(obj.html() != jQuery("#catHolder").find(".secondLevel.isOpen").html()){
			toggleMenu(jQuery("#catHolder").find(".secondLevel.isOpen"));
		}
	}
}
