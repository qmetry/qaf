jQuery(document).ready(function(){'use strict';window.prettyPrint&&prettyPrint();jQuery('.dropdown-toggle').click(function(e){e.preventDefault();setTimeout(jQuery.proxy(function(){if('ontouchstart'in document.documentElement){jQuery(this).siblings('.dropdown-backdrop').off().remove();}},this),0);});jQuery('.accordion-body').on('show',function(e){jQuery(e.currentTarget).parent().find('.accordion-heading').addClass('active')});jQuery('.accordion-body').on('hide',function(e){jQuery(e.currentTarget).parent().find('.accordion-heading').removeClass('active')});jQuery('.content').fitVids();jQuery('.background-video').each(function(){var el=jQuery(this);el.videobackground({videoSource:[[el.attr('data-mp4'),'video/mp4'],[el.attr('data-webm'),'video/webm'],[el.attr('data-ogv'),'video/ogg']],poster:el.attr('data-image'),loop:true,resize:false,controlPosition:'hide',preload:'auto'});});function thegravity_fade_texts(){jQuery('.fadeFromTop').delay(1000).animate({top:'0',opacity:1},'slow');jQuery('.fadeFromBottom').delay(1000).animate({top:'0',opacity:1},'slow');jQuery('.fastFadeFromTop').delay(300).animate({top:'0',opacity:1},'slow');jQuery('.fastFadeFromBottom').delay(300).animate({top:'0',opacity:1},'slow');}
var pagetitle=jQuery('.page-title');if(pagetitle.length){var images=jQuery('img, .page-title');jQuery.each(images,function(){var el=jQuery(this),image=el.css('background-image').replace(/"/g,'').replace(/url\(|\)$/ig,'');if(image&&image!==''&&image!=='none')
images=images.add(jQuery('<img>').attr('src',image));if(el.is('img'))
images=images.add(el);});images.imagesLoaded(thegravity_fade_texts);}else{thegravity_fade_texts();}
jQuery('[data-toggle=tooltip]').tooltip();var navbarFixedTop=jQuery('.navbar-fixed-top'),contactBarBtn=jQuery('.contact-bar-btn'),contactBar=jQuery('.contact-bar'),contactCorner=jQuery('.contact-bar-corner'),searchBarBtn=jQuery('.search-bar-btn'),searchBar=jQuery('.search-bar'),searchBarCorner=jQuery('.search-bar-corner'),closeBtn=jQuery('.search-bar .close, .contact-bar .close'),projectNavBar=jQuery('.project-navbar'),projectNav=jQuery('.project-nav'),content=jQuery('.content'),footer=jQuery('footer');contactBar.css({top:-contactBar.height()});searchBar.css({top:-searchBar.height()});if(footer.length){content.css({marginBottom:footer.height()});}
function contact_close(){contactBar.animate({top:-contactBar.height()},'slow');contactCorner.css('display','none');}
function search_close(){searchBar.animate({top:-searchBar.height()},'slow');searchBarCorner.css('display','none');}
contactBarBtn.click(function(e){search_close();contactBar.show();navbarFixedTop.animate({top:contactBar.height()},'slow'),contactBar.animate({top:'0'},'slow'),stickyHeader.addClass('sticky'),contactCorner.css('display','block'),e.preventDefault();return false;});closeBtn.click(function(e){navbarFixedTop.animate({top:'0'},'slow'),searchBar.animate({top:-searchBar.height()},'slow'),contactBar.animate({top:-contactBar.height()},'slow'),stickyHeader.removeClass('sticky'),searchBarCorner.css('display','none'),contactCorner.css('display','none'),e.preventDefault();return false;});searchBarBtn.click(function(e){contact_close();searchBar.show();navbarFixedTop.animate({top:searchBar.height()},'slow'),searchBar.animate({top:'0'},'slow'),stickyHeader.addClass('sticky'),searchBarCorner.css('display','block'),e.preventDefault();return false;});var stickyHeader=jQuery('.navbar-fixed-top');jQuery(window).scroll(function(){if(stickyHeader.offset().top>100){stickyHeader.addClass('sticky')}else{stickyHeader.removeClass('sticky')}});var fader=jQuery('.fader');jQuery(window).scroll(function(){var percent=jQuery(document).scrollTop()/200;fader.css('opacity',1-percent);});function images_resize(){jQuery('.grid-item').each(function(){var $el=jQuery(this),width=$el.width(),$img=$el.find('img');if($img.length&&$img.data().processed===undefined){var imgw=$img.width(),imgh=$img.height();if((imgw*0.7)>=imgh){$img.height(Math.round(width*0.7));$img.width(Math.round($img.height()*imgw/imgh));}else{$img.width(width);if($el.hasClass('project-fullwidth')){$el.height(Math.round(width*0.7));}
$img.height(Math.round($img.width()*imgh/imgw));}
$img.data('processed',true);}});}
jQuery(window).smartresize(function(){images_resize();});var filtersSet=jQuery('.filters-set'),$container=jQuery('.filterable-items > .row, .filterable-items > .row-fluid');jQuery('.filters-btn').on('mouseenter',function(){if(filtersSet.is(':hidden')){filtersSet.fadeIn('normal');}});jQuery('filters-btn').click(function(){return false;});jQuery('.filters').on('mouseleave',function(){filtersSet.fadeOut('normal');});jQuery(window).smartresize(function(){$container.isotope();});$container.imagesLoaded(function(){jQuery(window).smartresize();});filtersSet.find('a').click(function(){var selector=jQuery(this).attr('data-filter');filtersSet.find('a').removeClass('current');jQuery(this).addClass('current');$container.isotope({filter:selector});return false;});$container.infinitescroll({navSelector:'#page-nav',nextSelector:'#page-nav a',itemSelector:'.filterable-items > .row > div, .filterable-items > .row-fluid > div',loading:{finishedMsg:theme.txt_nomoreitems,img:theme.theme_url+'/img/loader.gif',msgText:theme.txt_loadingnewitems}},function(newElements){var $newElems=jQuery(newElements).css({opacity:0});$newElems.imagesLoaded(function(){images_resize();$newElems.animate({opacity:1});$container.isotope('appended',$newElems,true);});});jQuery(window).unbind('.infscr');jQuery('#load-more-items').click(function(){$container.infinitescroll('retrieve');return false;});jQuery(window).stellar({horizontalScrolling:false,verticalScrolling:true,horizontalOffset:0,verticalOffset:0,responsive:false,scrollProperty:'scroll',positionProperty:'transform',parallaxBackgrounds:true,parallaxElements:true,hideDistantElements:false,hideElement:function($elem){$elem.hide();},showElement:function($elem){$elem.show();}});var $toTop=jQuery('#to-top');$toTop.hide();jQuery(window).scroll(function(){if(jQuery(this).scrollTop()>200){$toTop.show();}else{$toTop.hide();}});$toTop.click(function(){jQuery('body, html').animate({scrollTop:0},600);return false;});jQuery('.services-small').click(function(){if(jQuery(this).attr('href')){return true;}
var tag=jQuery(this).attr('data-tag'),pos=jQuery('.services-big[data-tag="'+tag+'"]').offset();if(pos){jQuery('body, html').animate({scrollTop:pos.top>100?pos.top-100:0},600);}
return false;});jQuery('.flexslider').flexslider({namespace:'flex-',selector:'.slides > li',animation:'fade',easing:'swing',direction:'horizontal',reverse:false,animationLoop:true,smoothHeight:false,startAt:0,slideshow:true,slideshowSpeed:7000,animationSpeed:600,initDelay:0,randomize:false,pauseOnAction:true,pauseOnHover:false,useCSS:true,touch:true,video:false,controlNav:true,directionNav:true,prevText:'',nextText:'',keyboard:true,multipleKeyboard:false,mousewheel:false,pausePlay:false,pauseText:'Pause',playText:'Play',controlsContainer:'',manualControls:'',sync:'',asNavFor:'',itemWidth:0,itemMargin:0,minItems:0,maxItems:0,move:0,start:function(){},before:function(){},after:function(){},end:function(){},added:function(){},removed:function(){}});jQuery('.like-btn').click(function(){var el=jQuery(this),postid=el.attr('data-postid');if(!el.hasClass('active')){jQuery.post(theme.ajax_url,{action:'portfolioitem_like',postid:postid},function(response){if(response){el.find('span').html(response);el.addClass('active');el.unbind('click');el.click(function(){return false;});}});}
return false;});jQuery('form.contacter').each(function(){var el=jQuery(this),button=el.find('#contacter-button'),mnonce=el.attr('data-nonce');button.click(function(){var mname=el.find('#contacter-name'),memail=el.find('#contacter-email'),mmessage=el.find('#contacter-message');function check_empty(el){if(el.val().length==0){el.addClass('error');}else{el.removeClass('error');}}
check_empty(mname);check_empty(memail);check_empty(mmessage);if(mname.val().length>0&&memail.val().length>0&&mmessage.val().length>0){jQuery.post(theme.ajax_url,{action:'contactform_sendmessage',name:mname.val(),email:memail.val(),message:mmessage.val(),nonce:mnonce},function(response){jQuery('#contact-form').modal('hide');if(response){jQuery('#contact-form-success').modal();}else{jQuery('#contact-form-fail').modal();}});}
return false;});});var gmaps=jQuery('.google-map');if(gmaps.length>0){jQuery.getScript('https://www.google.com/jsapi',function(){google.load('maps','3',{other_params:'sensor=false',callback:function(){gmaps.each(function(){var target=jQuery(this);var title=target.attr('data-title');var map=null;var latlng=new google.maps.LatLng(target.attr('data-lat'),target.attr('data-lng')),mapOptions={zoom:parseInt(target.attr('data-zoom'),10)||10,center:latlng,panControl:false,scrollwheel:false,zoomControl:true,zoomControlOptions:{style:google.maps.ZoomControlStyle.SMALL,position:google.maps.ControlPosition.LEFT_BOTTOM},scaleControl:false,mapTypeControl:false,mapTypeControlOptions:{mapTypeIds:[google.maps.MapTypeId.ROADMAP,'map_style']}};map=new google.maps.Map(this,mapOptions);var infowindow=new google.maps.InfoWindow({content:'<div id="google-map-marker">'+title+'</div>'});var marker=new google.maps.Marker({position:latlng,map:map,title:title});google.maps.event.addListener(marker,'click',function(){infowindow.open(map,marker);});var height=target.attr('data-height');if(jQuery.isNumeric(height)&&height>0){target.height(height);}else{target.height(target.width()*3/8);}});}});});}
//jQuery('#tabwrap').tabs({ fxFade: true, fxSpeed: 'slow' });
		/*var type = window.location.hash;
		if(type.length > 1) {
			var scrollto = eval(jQuery(type).offset().top  - 140);
			jQuery('html, body').animate({
			   	scrollTop: scrollto
			}, 1000);
		}*/
		jQuery(document).ready(function() {
			
			jQuery('html, body').hide();

		    if (window.location.hash) {
		        setTimeout(function() {
		            jQuery('html, body').scrollTop(0).show();
		            jQuery('html, body').animate({
		                scrollTop: jQuery(window.location.hash).offset().top - 110
		                }, 1000)
		        }, 0);
		    }
		    else {
		        jQuery('html, body').show();
		    }


		});

			jQuery('.circular_nav .node .cnav').hover(function(){
				jQuery('.circular_nav .node .cnav').removeClass('active');
				jQuery(this).addClass('active');
			});
			jQuery('.circular_nav .node').hover(function(){
				var abc = jQuery(this).attr('data-title');
				//alert(abc);
				jQuery('.nac_content .cont').hide();
				jQuery('.nac_content #'+abc+'.cont').show();
			});

			 jQuery('img.svg').each(function(){
			    var $img = jQuery(this);
			    var imgID = $img.attr('id');
			    var imgClass = $img.attr('class');
			    var imgURL = $img.attr('src');

			    jQuery.get(imgURL, function(data) {
			        // Get the SVG tag, ignore the rest
			        var $svg = jQuery(data).find('svg');

			        // Add replaced image's ID to the new SVG
			        if(typeof imgID !== 'undefined') {
			            $svg = $svg.attr('id', imgID);
			        }
			        // Add replaced image's classes to the new SVG
			        if(typeof imgClass !== 'undefined') {
			            $svg = $svg.attr('class', imgClass+' replaced-svg');
			        }

			        // Remove any invalid XML tags as per http://validator.w3.org
			        $svg = $svg.removeAttr('xmlns:a');

			        // Replace image with new SVG
			        $img.replaceWith($svg);

			    }, 'xml');

			});
			jQuery(function(){
				// Enabling Popover on homepage (sexywheel) pin1, pin2, pin3, pin4, pin5
			    jQuery("#one").popover({ html : true, content: function() { return jQuery('#pinOneContent').html(); } });
			    jQuery("#two").popover({ html : true, content: function() { return jQuery('#pinTwoContent').html(); } });
				jQuery("#three").popover({ html : true, content: function() { return jQuery('#pinThreeContent').html(); } });
				jQuery("#four").popover({ html : true, content: function() { return jQuery('#pinFourContent').html(); } });
				jQuery("#five").popover({ html : true, content: function() { return jQuery('#pinFiveContent').html(); } });
				jQuery("#six").popover({ html : true, content: function() { return jQuery('#pinSixContent').html(); } });
				jQuery("#seven").popover({ html : true, content: function() { return jQuery('#pinSevenContent').html(); } });
				jQuery("#eight").popover({ html : true, content: function() { return jQuery('#pinEightContent').html(); } });
				jQuery("#nine").popover({ html : true, content: function() { return jQuery('#pinNineContent').html(); } });
				jQuery("#ten").popover({ html : true, content: function() { return jQuery('#pinTenContent').html(); } });
				jQuery("#eleven").popover({ html : true, content: function() { return jQuery('#pinElevenContent').html(); } });
			});

			/* QMetry Test Manager Page - Improve QA Efficiency section */
		    jQuery('.tabnav .t').click(function() {
		        jQuery('.tabnav .t').removeClass('active');
		        jQuery(this).addClass('active');

		        var dataname = jQuery(this).attr('data-title');
		        //alert(dataname);
		        jQuery('.tabscontainer .tabscont .tb').removeClass('active');

		        jQuery('#' + dataname + '.tb').addClass('active');
		    });
		    jQuery('.tabscont .resp_title a').click(function() {
		        jQuery('.tabscont .resp_title a').removeClass('activelink');
		        jQuery(this).addClass('activelink');

		        var dataname = jQuery(this).attr('data-title');
		        //alert(dataname);
		        jQuery('.tabscontainer .tabscont .tb').removeClass('active');
		        jQuery('#' + dataname + '.tb').addClass('active');

		    });
		    
		    

			jQuery('#ri-grid1').gridrotator({
				w1024:{ rows : 2, columns : 2 },
				w768:{ rows : 2, columns : 2 },
				w480:{ rows : 2, columns : 2 },
				w320:{ rows : 2, columns : 1 },
				w240:{ rows : 2, columns : 1 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid2').gridrotator({
				w1024:{ rows : 1, columns : 1 },
				w768:{ rows : 1, columns : 1 },
				w480:{ rows : 1, columns : 1 },
				w320:{ rows : 1, columns : 1 },
				w240:{ rows : 1, columns : 1 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid3').gridrotator({
				w1024:{ rows : 2, columns : 2 },
				w768:{ rows : 2, columns : 2 },
				w480:{ rows : 2, columns : 2 },
				w320:{ rows : 2, columns : 2 },
				w240:{ rows : 2, columns : 2 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid4').gridrotator({
				w1024:{ rows : 2, columns : 1 },
				w768:{ rows : 2, columns : 1 },
				w480:{ rows : 2, columns : 1 },
				w320:{ rows : 2, columns : 1 },
				w240:{ rows : 2, columns : 1 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid5').gridrotator({
				w1024:{ rows : 1, columns : 1 },
				w768:{ rows : 1, columns : 1 },
				w480:{ rows : 1, columns : 1 },
				w320:{ rows : 1, columns : 1 },
				w240:{ rows : 1, columns : 1 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid6').gridrotator({
				w1024:{ rows : 2, columns : 2 },
				w768:{ rows : 2, columns : 2 },
				w480:{ rows : 2, columns : 2 },
				w320:{ rows : 2, columns : 2 },
				w240:{ rows : 2, columns : 2 },
				nochange : [],
				preventClick : false
			});
			jQuery('#ri-grid7').gridrotator({
				w1024:{ rows : 2, columns : 1 },
				w768:{ rows : 2, columns : 1 },
				w480:{ rows : 2, columns : 1 },
				w320:{ rows : 2, columns : 1 },
				w240:{ rows : 2, columns : 1 },
				nochange : [],
				preventClick : false
			});


	jQuery(function(){

    	jQuery("[data-toggle=popover]").popover();

    	jQuery("#a_loc1").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc1Content').html();
	        }
	    });
	    jQuery("#a_loc2").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc2Content').html();
	        }
	    });
	    jQuery("#a_loc3").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc3Content').html();
	        }
	    });
	    jQuery("#a_loc4").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc4Content').html();
	        }
	    });
	    jQuery("#a_loc5").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc5Content').html();
	        }
	    });
	    jQuery("#a_loc6").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc6Content').html();
	        }
	    });

    	// Enabling Popover on homepage (sexywheel) pin1, pin2, pin3, pin4, pin5
	    jQuery("#pin1").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#pin1Content').html();
	        }
	    });
	    jQuery("#pin2").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#pin2Content').html();
	        }
	    });
	    jQuery("#pin3").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#pin3Content').html();
	        }
	    });
	    jQuery("#pin4").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#pin4Content').html();
	        }
	    });
	    jQuery("#pin5").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#pin5Content').html();
	        }
	    });
	    /*jQuery("#pin1").popover('show');*/
	    jQuery("#loc1").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc1Content').html();
	        }
	    });
	    jQuery("#loc2").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc2Content').html();
	        }
	    });
	    jQuery("#loc3").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc3Content').html();
	        }
	    });
	    jQuery("#loc4").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc4Content').html();
	        }
	    });
	    jQuery("#loc5").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc5Content').html();
	        }
	    });
	    jQuery("#loc6").popover({
	        html : true, 
	        content: function() {
	          return jQuery('#loc6Content').html();
	        }
	    });
	});

	var sbwidth = jQuery('.cust_3 .bx-viewport').width();
	
	if(sbwidth <=580){
		//alert('one');
		jQuery('.cust_3 .slide').css('width',sbwidth);
	}
	else if (sbwidth>580 && sbwidth<=900){
		//alert('two');
		jQuery('.cust_3 .slide').css('width',sbwidth/2);
	}
	else if (sbwidth>900 && sbwidth<=3300){
		//alert('Three');
		jQuery('.cust_3 .slide').css('width',sbwidth/3);
	}


	var sbwidth1 = jQuery('.get-result-tab .bx-viewport').width();
	if(sbwidth1 <=580){
		//alert('one');
		jQuery('.get-result-tab .slide').css('width',sbwidth1);
	}
	else if (sbwidth1>580 && sbwidth1<=900){
		//alert('two');
		jQuery('.get-result-tab .slide').css('width',sbwidth1/2);
	}
	else if (sbwidth1>900 && sbwidth1<=3300){
		//alert('Three');
		jQuery('.get-result-tab .slide').css('width',sbwidth/3);
	}	

	/* home page bxslider */
	jQuery('.bxslider1').bxSlider({
	  minSlides: 3,
	  maxSlides: 10,
	  slideWidth: 170,
	  slideMargin: 10
	});

	/* QMetry page customers logo bxslider */
	jQuery('.bxslider2').bxSlider({
	  minSlides: 3,
	  maxSlides: 5,
	  slideWidth: 200, /* 190 */
	  infiniteLoop: true,
	  pager:false,
	});

	jQuery('.bxslider3').bxSlider({
	minSlides: 3,
	maxSlides: 4,
	slideWidth: 250,
	slideMargin: 0
	});

	jQuery('.bxslider4').bxSlider({
	minSlides: 3,
	maxSlides: 4,
	slideWidth: 250,
	slideMargin: 10
	});

	jQuery('.arrow1').hide();
	jQuery('.arrow5').hide();

	jQuery('.circle .slice').hover(function(){
		jQuery('.circle .slice').removeClass('current');
		jQuery(this).addClass('current');

		var processName = jQuery(this).attr('data-title');
		jQuery('.moreinfo .box').removeClass('active');
		jQuery('#'+processName+'.box').addClass('active');

		if(processName == 'mobstra'){
			jQuery('.arrow').show();
			jQuery('.arrow1').hide();
			jQuery('.arrow5').hide();
		}else if(processName == 'mobdev'){
			jQuery('.arrow').show();
			jQuery('.arrow1').hide();
			jQuery('.arrow2').hide();
		}else if(processName == 'mobtest'){
			jQuery('.arrow').show();
			jQuery('.arrow2').hide();
			jQuery('.arrow3').hide();
		}else if(processName == 'mobauto'){
			jQuery('.arrow').show();
			jQuery('.arrow3').hide();
			jQuery('.arrow4').hide();
		}else if(processName == 'mobopt'){
			jQuery('.arrow').show();
			jQuery('.arrow4').hide();
			jQuery('.arrow5').hide();
		}
	});
	
	jQuery('.circle .unsel').click(function(){
		jQuery('.circle .slice').removeClass('current');
		jQuery('.moreinfo .box').removeClass('active');
		jQuery('.arrow').show();
	});

	jQuery(document).mouseup(function (e)
	{
	    var container = jQuery(".sexy_wheel .circle");

	    if (!container.is(e.target) // if the target of the click isn't the container...
	        && container.has(e.target).length === 0) // ... nor a descendant of the container
	    {
	        //jQuery(".sexy_wheel .circle .slice").removeClass('current');
	        //jQuery(".moreinfo .box").removeClass('active');
	        //jQuery('.arrow').show();
	    }
	});


	/* QMetry page customers saying bxslider */
	jQuery('.customer_slider').bxSlider({
		minSlides: 1,
		maxSlides: 1,
		pager:true,
		adaptiveHeight: true
	});

	/* Testimonial Customer Slider BX Slider - Testimonials Page */
	jQuery('.testi_slider .tslider').bxSlider({
		minSlides: 1,
		maxSlides: 1,
		moveSlides: 1,
		infiniteLoop: true,
		pager: true,
		hideControlOnEnd: false,
		easing: easing,
		speed: speed,
		adaptiveHeight: true
	});

	/* Customers Saying BX Slider - Customer Page */
	var myNav = navigator.userAgent.toLowerCase();
	var easing = "cubic-bezier(0.86, 0, 0.07, 1)";
	var speed = 1000;
	var iever, easing, speed;
	if(myNav.indexOf('msie') != -1){
		iever = parseInt(myNav.split('msie')[1]);
		if(iever < 10){
			easing = "linear";
			speed = 500;
		}
	}
	jQuery('.cust_saying_slider').bxSlider({
		onSliderLoad: function(){
			jQuery('.cust_saying_slider').css("visibility", "visible");
		},
		minSlides: 1,
		maxSlides: 1,
		infiniteLoop: false,
		hideControlOnEnd: true,
		adaptiveHeight: true,
		/*adaptiveHeightSpeed: speed,*/
		easing: easing,
		speed: speed,
	});

	/* Customers Results BX Slider - Customer Page */
	jQuery('.cust_results_slider').bxSlider({
		slideWidth: 326,
		minSlides: 1,
		maxSlides: 3,
		moveSlides: 3,
		infiniteLoop: false,
		hideControlOnEnd: true,
		easing: easing,
		speed: speed
	});

	/* Hear it directly from customers BX Slider - Customer Page */
	jQuery('.cust_hear_slider').bxSlider({
		minSlides: 1,
		maxSlides: 1,
		slideMargin: 20,
		infiniteLoop: false,
		hideControlOnEnd: true,
		easing: easing,
		speed: speed
	});
	jQuery('.historytimeline .slider').bxSlider({
		minSlides: 3,
		maxSlides: 3,
		infiniteLoop: false,
		hideControlOnEnd: true,
		easing: easing,
		speed: speed
	});

	/* New Home page banner bx slider (new home page is removed) */
	var setWidthforBanner = jQuery(window).width();
	jQuery('.sbanner').bxSlider({
	    responsive: true,
	    pager:true,
	    adaptiveHeight: false,
	    touchEnabled:true,
	    infiniteLoop: false,
   		hideControlOnEnd: true,
   		startSlide: 0,
	    speed:1500,
	    /*auto: true,*/
	    auto : (setWidthforBanner < 768) ? false : true,
	    pause:5000,
	    autoControls: false,
	    moveSlideQty: 1,
	    /*useCSS: false,*/
	    autoDelay: 0
	});

	var setWidth = jQuery(window).width();
	jQuery('.moresuccess .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 0,
		maxSlides: 2,
		infiniteLoop: false,
		adaptiveHeight: false,
		responsive:true,
		controls : (setWidth > 1024) ? ((jQuery(".moresuccess .slider>.slide").length > 3) ? true: false) : ((jQuery(".moresuccess .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidth > 1024) ? ((jQuery(".moresuccess .slider>.slide").length > 3) ? true: false) : ((jQuery(".moresuccess .slider>.slide").length > 1) ? true: false)
	});

	var setWidth_mobi_init = jQuery(window).width();
	jQuery('.mobi_init .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 1,
		/*maxSlides: (setWidth_mobi_init > 1024 ? 3 : ((setWidth_mobi_init < 1024 && setWidth_mobi_init > 768) ? 2 : 1 )),*/
		maxSlides: 2,
		moveSlides:1,
		infiniteLoop: false,
		adaptiveHeight: true,
		responsive:true,
		controls : (setWidth_mobi_init > 1024) ? ((jQuery(".mobi_init .slider>.slide").length > 3) ? true: false) : ((jQuery(".mobi_init .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidth_mobi_init > 1024) ? ((jQuery(".mobi_init .slider>.slide").length > 3) ? true: false) : ((jQuery(".mobi_init .slider>.slide").length > 1) ? true: false)
	});

	var setWidthmarketleaders = jQuery(window).width();
	jQuery('.marketleaders .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 0,
		maxSlides: 2,
		infiniteLoop: false,
		adaptiveHeight: false,
		responsive:true,
		controls : (setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false)
		/*(setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false)*/
	});

	var setWidthmobijourney = jQuery(window).width();
	jQuery('.mobijourney .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 0,
		maxSlides: 2,
		infiniteLoop: false,
		adaptiveHeight: false,
		responsive:true,
		controls : (setWidthmobijourney > 1024) ? ((jQuery(".mobijourney .slider>.slide").length > 3) ? true: false) : ((jQuery(".mobijourney .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidthmobijourney > 1024) ? ((jQuery(".mobijourney .slider>.slide").length > 3) ? true: false) : ((jQuery(".mobijourney .slider>.slide").length > 1) ? true: false)
		/*(setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false)*/
	});
	var setWidthhelpindus = jQuery(window).width();
	jQuery('.helpindus .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 0,
		maxSlides: 2,
		infiniteLoop: false,
		adaptiveHeight: false,
		responsive:true,
		controls : (setWidthhelpindus > 1024) ? ((jQuery(".helpindus .slider>.slide").length > 3) ? true: false) : ((jQuery(".helpindus .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidthhelpindus > 1024) ? ((jQuery(".helpindus .slider>.slide").length > 3) ? true: false) : ((jQuery(".helpindus .slider>.slide").length > 1) ? true: false)
		/*(setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false)*/
	});
	var setWidthleadorgspeed = jQuery(window).width();
	jQuery('.leadorgspeed .slider').bxSlider({
	    slideWidth: 310,
        minSlides: 0,
		maxSlides: 2,
		infiniteLoop: false,
		adaptiveHeight: false,
		responsive:true,
		controls : (setWidthleadorgspeed > 768) ? ((jQuery(".leadorgspeed .slider>.slide").length > 2) ? true: false) : ((jQuery(".leadorgspeed .slider>.slide").length > 1) ? true: false),
		hideControlOnEnd: true,
		pager : (setWidthleadorgspeed > 768) ? ((jQuery(".leadorgspeed .slider>.slide").length > 2) ? true: false) : ((jQuery(".leadorgspeed .slider>.slide").length > 1) ? true: false)
		/*(setWidthmarketleaders > 1024) ? ((jQuery(".marketleaders .slider>.slide").length > 3) ? true: false) : ((jQuery(".marketleaders .slider>.slide").length > 1) ? true: false)*/
	});

	
	jQuery(".video").click(function() {
		jQuery.fancybox({
			'padding'		: 0,
			'autoScale'		: false,
			'scrolling'		: 'auto',
			'transitionIn'	: 'none',
			'closeBtn'		: true,
			'transitionOut'	: 'none',
			'width'			: 880,
			'height'		: auto,
			'href'			: this.href.replace(new RegExp("watch\\?v=", "i"), 'v/'),
			'type'			: 'swf',
			'swf'			: {
				'wmode'				: 'transparent',
				'allowfullscreen'	: 'true'
			}
		});
		return false;
	});

	/* show product popup in footer bottom-right corner */
	jQuery(window).on("scroll", function() {
		if (jQuery(this).scrollTop()>100){
            jQuery('.homesliders .bx-default-pager').hide();  
        }else{
            jQuery('.homesliders .bx-default-pager').show();
        }	
	    var toBottom = jQuery(document).height() - (jQuery(document).scrollTop() + jQuery(window).height());
	    /*1700 > toBottom && toBottom > 200 ? jQuery(".popup-product").addClass("visible") : (200 > toBottom || toBottom > 1700) && jQuery(".popup-product").removeClass("visible")*/
		2800 > toBottom && toBottom > 200 ? jQuery(".popup-product").addClass("visible") : (200 > toBottom || toBottom > 1700) && jQuery(".popup-product").removeClass("visible");
	});
	jQuery( "a#popup_close" ).click(function(e) {
		jQuery("#additional-content").remove();
	});

	/* Required for Marketo Forms */
	jQuery.ajax({
	  url: '//munchkin.marketo.net/munchkin.js',
	  dataType: 'script',
	  cache: true,
	  success: function() {
	    Munchkin.init('984-PED-574');
	  }
	});

	/* Events page bxslider inside tabbing */
	var config = {
        slideWidth: 310,
        minSlides: 1,
		maxSlides: 3,
		infiniteLoop: false,
		adaptiveHeight: true,
		controls:false
    };
	
	/* Costomers page bxslider inside tabbing */
	var configGetResults = {
        slideWidth: 310,
        minSlides: 1,
		maxSlides: 3,
		infiniteLoop: false,
		adaptiveHeight: true,
		controls:true,
		hideControlOnEnd: true,
		pager: true
    };
	
    var sliders = new Array();
	jQuery('.events-tabs .slider').not(this).each(function(i, slider) {
	    sliders[i] = jQuery(slider).bxSlider();
	});
	jQuery.each(sliders, function(i, slider) {
       	slider.reloadSlider(config);
   	});
   
   	var sliders1 = new Array();
	jQuery('.cust-tabs .slider').not(this).each(function(i1, slider1) {
	    sliders1[i1] = jQuery(slider1).bxSlider();
	});
	jQuery.each(sliders1, function(i1, slider1) {
       	slider1.reloadSlider(configGetResults);
   	});

   	jQuery('.celebration.codeon .box .img, .celebration.lifeonfriday .box .img').hover(function(){
   		jQuery(this).addClass('zimg');
   	},function(){
   		jQuery(this).removeClass('zimg');
   	});

	
	jQuery('.resp_title a').click(function(){
		if(jQuery(this).hasClass('activelink')){
	        jQuery(this).removeClass('activelink');
	        return;
	    }else{
	    	jQuery('.resp_title a').removeClass('activelink');
	    	jQuery(this).addClass('activelink');
	    }
	});
    jQuery('.events-tabs .nav-tabs li a, .events-tabs .tab-content .resp_title a, .cust-tabs .nav-tabs li a, .cust-tabs .tab-content .resp_title a, .gets-tabs .nav-tabs li a, .gets-tabs .tab-content .resp_title a').click(function(){
    	var tabsnName = jQuery(this).attr('data-title');
    	jQuery('.tab-pane').removeClass('active in');
    	jQuery('#'+tabsnName+'.tab-pane').toggleClass('active in');

    	jQuery.each(sliders, function(i, slider) {
        	slider.reloadSlider(config);
	    });
		
		jQuery.each(sliders1, function(i1, slider1) {
        	slider1.reloadSlider(configGetResults);
	    });
		
    	sliderBehave();
    });

    /* Events page bxslider inside tabbing */
    jQuery('.events-tabs .resp_title').click(function(){
    	jQuery('.events-tabs .resp_title').removeClass('activelink');
    	jQuery(this).addClass('activelink');
    });

 	jQuery('.cust-tabs .resp_title').click(function(){
    	jQuery('.cust-tabs .resp_title').removeClass('activelink');
    	jQuery(this).addClass('activelink');
    });

    jQuery('.gets-tabs .resp_title').click(function(){
    	jQuery('.gets-tabs .resp_title').removeClass('activelink');
    	jQuery(this).addClass('activelink');
    });

	jQuery(window).load(function() {
		sliderBehave();
		// Fixing heights of elements dynamically on page load
		jQuery('.home-box').equalize(30);
		jQuery('.lblog .box .title').equalize();
		jQuery('.lblog .box .desc .date').equalize();
		jQuery('.lblog .box .desc .text').equalize();
		jQuery('.newsnevent .box .title').equalize();
		jQuery('.newsnevent .box .desc').equalize();
		jQuery('.c_list .box').equalize(100);
		jQuery('.aw_list .box').equalize(30);
		jQuery('.cust_logos .box').equalize();
		jQuery('.footermar .col').equalize();
		/*jQuery('.methodology .box .title').equalize();*/
		jQuery('.methodology .box .desc').equalize();
		jQuery('.strat_mobi .strat .img').equalize();
		jQuery('.strat_mobi_pg .box .title').equalize();
		jQuery('.strat_mobi_pg .box .desc').equalize();
		jQuery('.moresuccess .slide .box .img').equalize();
		jQuery('.moresuccess .slide .box p').equalize();
		jQuery('.marketleaders .slide .box .img').equalize();
		jQuery('.marketleaders .slide .box p').equalize();
		jQuery('.mobispec .exppoints .col').equalize();
		jQuery('.o_locations .box .details .head').equalize();
		jQuery('.o_locations .box .details .desc').equalize();
		jQuery('.mob_serv_list .box .img').equalize();
		jQuery('.mob_serv_list .box .desc').equalize();
		jQuery('.checkmarks .box .desc').equalize();
		jQuery('.int_map .qualities .box .name').equalize();
		jQuery('.apps_tested .app_features .box .name').equalize();
		jQuery('.conn_devices .requesttrial .box .info p').equalize();
		jQuery('.cap_list .box .decs').equalize();
		jQuery('.bestpractice .box .text').equalize();
		jQuery('.yourefforts .effortareas .title').equalize();
		jQuery('.yourefforts .effortareas .desc').equalize();
		jQuery('.mobijourney .slide .box p').equalize();
		jQuery('.helpindus .slide .box p').equalize();
		jQuery('.leadorgspeed .slide .box p').equalize();
		jQuery('.mob_landscape .box .desc').equalize();
		jQuery('.graph_pair').equalize();
		jQuery('.pg_globalinfra .threepoints .box').equalize(40);
		jQuery('.personalmobility .desc').equalize();
		jQuery('.ourpeers .box').equalize(30);
		jQuery('.pg_career_benefits .benefits .box .desc').equalize();
		jQuery('.givinback .box').equalize(20);
		jQuery('.qas_4 h4').equalize();
		jQuery('.qmetry_6 .ul').equalize();
		jQuery('.newsfeeds .box .title').equalize();
		jQuery('.newsfeeds .box').equalize();
		jQuery('.sel_framework .box .img').equalize();
		jQuery('.sel_framework .box .desc').equalize();
		jQuery('.iotimpact .box .img').equalize();
		jQuery('.iotimpact .box .cont .desc').equalize();
		jQuery('.benefitsqmetryjira .ul').equalize();
		jQuery('.whyjira_list .box .decs').equalize();
		jQuery('.benefitsqmetryjira h4').equalize();
		jQuery('.benefitsqmetryjira .ul').equalize();
		jQuery('.megamenu .box .text').equalize();
		jQuery('.resource_dc .rec_d_box .rec_d_title').equalize();
		jQuery('.resource_dc .rec_d_box .rec_d_text').equalize();
		jQuery('.mobile_revolution .container .col-md-6').equalize();
		jQuery('.grid figcaption .desc').equalize();
		jQuery('.fourchallenges .box').equalize(20);
		jQuery('.newera .fourpoints .box .title').equalize();
		jQuery('.newera .fourpoints .box p').equalize();


		if(jQuery(window).width() >= 768){
			jQuery('.mobile-testing-testingtype .testing-blk').equalize();
			jQuery('.abt_2 .md_mobile_div').equalize();
			jQuery('.new_part_four div.row-fluid .box').equalize(20);
		}
		
		// Fixing heights of elements dynamically on page load
		jQuery(window).resize(function() {
			// Needs to be a timeout function so it doesn't fire every ms of resize
			setTimeout(function() {
				jQuery('.home-box').equalize(30);
				jQuery('.lblog .box .title').equalize();
				jQuery('.lblog .box .desc .date').equalize();
				jQuery('.lblog .box .desc .text').equalize();
				jQuery('.newsnevent .box .title').equalize();
				jQuery('.newsnevent .box .desc').equalize();
				jQuery('.c_list .box').equalize(100);
				jQuery('.aw_list .box').equalize(30);
				jQuery('.cust_logos .box').equalize();
				jQuery('.footermar .col').equalize();
				/*jQuery('.methodology .box .title').equalize();*/
				jQuery('.methodology .box .desc').equalize();
				jQuery('.strat_mobi .strat .img').equalize();
				jQuery('.strat_mobi_pg .box .title').equalize();
				jQuery('.strat_mobi_pg .box .desc').equalize();
				jQuery('.moresuccess .slide .box .img').equalize();
				jQuery('.moresuccess .slide .box p').equalize();
				jQuery('.marketleaders .slide .box .img').equalize();
				jQuery('.marketleaders .slide .box p').equalize();
				jQuery('.mobispec .exppoints .col').equalize();
				jQuery('.o_locations .box .details .head').equalize();
				jQuery('.o_locations .box .details .desc').equalize();
				jQuery('.mob_serv_list .box .img').equalize();
				jQuery('.mob_serv_list .box .desc').equalize();
				jQuery('.checkmarks .box .desc').equalize();
				jQuery('.int_map .qualities .box .name').equalize();
				jQuery('.apps_tested .app_features .box .name').equalize();
				jQuery('.conn_devices .requesttrial .box .info p').equalize();
				jQuery('.cap_list .box .decs').equalize();
				jQuery('.bestpractice .box .text').equalize();
				jQuery('.yourefforts .effortareas .title').equalize();
				jQuery('.yourefforts .effortareas .desc').equalize();
				jQuery('.mobijourney .slide .box p').equalize();
				jQuery('.helpindus .slide .box p').equalize();
				jQuery('.leadorgspeed .slide .box p').equalize();
				jQuery('.mob_landscape .box .desc').equalize();
				jQuery('.graph_pair').equalize();
				jQuery('.pg_globalinfra .threepoints .box').equalize(40);
				jQuery('.personalmobility .desc').equalize();
				jQuery('.ourpeers .box').equalize(30);
				jQuery('.pg_career_benefits .benefits .box .desc').equalize();
				jQuery('.givinback .box').equalize(20);
				jQuery('.qas_4 h4').equalize();
				jQuery('.qmetry_6 .ul').equalize();
				jQuery('.newsfeeds .box .title').equalize();
				jQuery('.newsfeeds .box').equalize();
				jQuery('.sel_framework .box .img').equalize();
				jQuery('.sel_framework .box .desc').equalize();
				jQuery('.iotimpact .box .img').equalize();
				jQuery('.iotimpact .box .cont .desc').equalize();
				jQuery('.benefitsqmetryjira .ul').equalize();
				jQuery('.whyjira_list .box .decs').equalize();
				jQuery('.benefitsqmetryjira h4').equalize();
				jQuery('.benefitsqmetryjira .ul').equalize();
				jQuery('.megamenu .box .text').equalize();
				jQuery('.resource_dc .rec_d_box .rec_d_title').equalize();
				jQuery('.resource_dc .rec_d_box .rec_d_text').equalize();
				jQuery('.mobile_revolution .container .col-md-6').equalize();
				jQuery('.grid figcaption .desc').equalize();
				jQuery('.fourchallenges .box').equalize(20);
				jQuery('.newera .fourpoints .box .title').equalize();
				jQuery('.newera .fourpoints .box p').equalize();
				

				if(jQuery(window).width() >= 768){
					jQuery('.mobile-testing-testingtype .testing-blk').equalize();
					jQuery('.abt_2 .md_mobile_div').equalize();
					jQuery('.new_part_four div.row-fluid .box').equalize(20);
				}
			}, 100);
		});

		jQuery('.qmetry_jira_tab .resp_title a').click(function(){
			jQuery('.qmetry_jira_tab .resp_title a').removeClass('activelink');
			jQuery(this).addClass('activelink');
		});
		jQuery('.qa_efficiency .resp_title a').click(function(){
			jQuery('.qa_efficiency .resp_title a').removeClass('activelink');
			jQuery(this).addClass('activelink');
		});

	});

jQuery(document).ready(function(){
    jQuery('a').click(function(){
        if(!jQuery(this).attr('onClick')){
        	jQuery('#fancybox-css').prop('href', 'http://172.99.67.169/infostretch_newsite/wp-content/plugins/easy-fancybox/fancybox/jquery.fancybox-1.3.6.pack.css');
        }
    });

    //Careerpage job sharing on social media
    /*if(window.location.hash) {
    	jQuery(window).trigger('resize');
        var hash = window.location.hash.substring(1); 
        jQuery(".fancybox-inline").attr("href", "#"+hash);
        jQuery(".fancybox-inline").fancybox({
            type: "inline",
            onClosed: function() {
                var url = window.location.href.split('#')[0];
                window.location.href = url;
            }
        }).trigger("click");
    }*/ 

    // Create the dropdown base
      jQuery("<select />").appendTo(".filters");
      
      // Create default option "Go to..."
      jQuery("<option />", {
         "selected": "selected",
         "value"   : "",
         "text"    : "Go to..."
      }).appendTo("nav select");
      
      // Populate dropdown with menu items
      jQuery("nav a").each(function() {
       var el = jQuery(this);
       jQuery("<option />", {
           "value"   : el.attr("href"),
           "text"    : el.text()
       }).appendTo("nav select");
      });
      
	   // To make dropdown actually work
	   // To make more unobtrusive: http://css-tricks.com/4064-unobtrusive-page-changer/
      jQuery("nav select").change(function() {
        window.location = jQuery(this).find("option:selected").val();
      });
    
});


	//Infrastructure page map location dot hover effect
	jQuery(".int_map .loc").hover(function(){
    	jQuery(this).addClass("anm");
 	},function(){
    	jQuery(this).removeClass("anm");
  	});

  	jQuery(".requesttrial .box").hover(function(){
    	jQuery(this).addClass("active");
 	},function(){
    	jQuery(this).removeClass("active");
  	});

  	/*jQuery(".pg_mapbox .mapbox .loc").click(function(){
  		jQuery(".pg_mapbox .mapbox .loc").removeClass("active");
    	jQuery(this).addClass("active");
    	var dt = jQuery(this).attr('data-title');
    	jQuery('.loc_map_details .infobox').removeClass('active');
    	jQuery('#'+dt+'.infobox').addClass('active');

 	});*/

 	jQuery('.s_locations .panel-title a').click(function(){
 		if(jQuery(this).hasClass('collapsed')){
 			jQuery(this).addClass('collapsed');
 			jQuery('.s_locations .panel-collapse.in').collapse('hide');
			jQuery('.s_locations .panel-title a').addClass('collapsed');
 			return true;
 		}else{
 			jQuery(this).addClass('collapsed');
 			return true;
 			
 		}
 	});

 	jQuery('.bycategory .panel-title a').click(function(){
 		if(jQuery(this).hasClass('collapsed')){
 			jQuery(this).addClass('collapsed');
 			jQuery('.bycategory .panel-collapse.in').collapse('hide');
			jQuery('.bycategory .panel-title a').addClass('collapsed');
 			return true;
 		}else{
 			jQuery(this).addClass('collapsed');
 			return true;
 			
 		}
 	});
 	jQuery('.tab-inside-joinus li a').click(function(){
	 	var abcv = jQuery(this).attr('data-title');

	 	jQuery('.tab-content .tab-pane').removeClass('in');
	 	jQuery('.tab-content #'+abcv+'').addClass('in');
	 	jQuery('.tab-inside-joinus').tabs('load', 0);
	});
	
});

function sliderBehave(){

	/** code for the events slider in events page ********/
	var tab1count = jQuery('.events-tabs #tab1 .slider .slide').size();
	var tab2count = jQuery('.events-tabs #tab2 .slider .slide').size();
	var tab3count = jQuery('.events-tabs #tab3 .slider .slide').size();
	var wwidth = jQuery(window).width();
	if(wwidth > 1024){
		if(tab1count < 4){
			jQuery('.events-tabs #tab1 .bx-pager').css('display','none');

		}
		if(tab2count < 4){
			jQuery('.events-tabs #tab2 .bx-pager').css('display','none');
		}
		if(tab3count < 4){
			jQuery('.events-tabs #tab3 .bx-pager').css('display','none');
		}
	}
	else if (wwidth < 1024 && wwidth > 768){
		if(tab1count < 3){
			jQuery('.events-tabs #tab1 .bx-pager').css('display','none');
		}
		if(tab2count < 3){
			jQuery('.events-tabs #tab2 .bx-pager').css('display','none');
		}
		if(tab3count < 3){
			jQuery('.events-tabs #tab3 .bx-pager').css('display','none');
		}
	}
	else {
		if(tab1count < 2){
			jQuery('.events-tabs #tab1 .bx-pager').css('display','none');
		}
		if(tab2count < 2){
			jQuery('.events-tabs #tab2 .bx-pager').css('display','none');
		}
		if(tab3count < 2){
			jQuery('.events-tabs #tab3 .bx-pager').css('display','none');	
		}

	}
	
	/** code for infostretch gets results page ***********/	
	var allTabcount = jQuery('.cust-tabs #tabAll .slider .slide').size();
	var tabOpticount = jQuery('.cust-tabs #tabOptimization .slider .slide').size();
	var tabAutocount = jQuery('.cust-tabs #tabAutomation .slider .slide').size();
	var tabQAcount = jQuery('.cust-tabs #tabTestQA .slider .slide').size();
	var tabStracount = jQuery('.cust-tabs #tabStrategy .slider .slide').size();
	var tabDevcount = jQuery('.cust-tabs #tabDevelopment .slider .slide').size();	
	var wwidth = jQuery(window).width();
	
	if(tabOpticount == 0)
	{
		// $(elem).reloadSlider();
	}
	if(wwidth > 1024){
		if(allTabcount < 4){
			jQuery('.cust-tabs #tabAll .bx-pager').css('display','none');
		}
		if(tabOpticount < 4){
			jQuery('.cust-tabs #tabOptimization .bx-pager').css('display','none');
		}
		if(tabAutocount < 4){
			jQuery('.cust-tabs #tabAutomation .bx-pager').css('display','none');
		}
		if(tabQAcount < 4){
			jQuery('.cust-tabs #tabTestQA .bx-pager').css('display','none');
		}
		if(tabStracount < 4){
			jQuery('.cust-tabs #tabStrategy .bx-pager').css('display','none');
		}
		if(tabDevcount < 4){
			jQuery('.cust-tabs #tabDevelopment .bx-pager').css('display','none');
		}
	}
	else if (wwidth < 1024 && wwidth > 768){
		if(allTabcount < 3){
			jQuery('.cust-tabs #tabAll .bx-pager').css('display','none');
		}
		if(tabOpticount < 3){
			jQuery('.cust-tabs #tabOptimization .bx-pager').css('display','none');
		}
		if(tabAutocount < 3){
			jQuery('.cust-tabs #tabAutomation .bx-pager').css('display','none');
		}
		if(tabStracount < 3){
			jQuery('.cust-tabs #tabStrategy .bx-pager').css('display','none');
		}
		if(tabQAcount < 3){
			jQuery('.cust-tabs #tabTestQA .bx-pager').css('display','none');
		}
		if(tabDevcount < 3){
			jQuery('.cust-tabs #tabDevelopment .bx-pager').css('display','none');
		}
	}
	else {
		if(allTabcount < 2){
			jQuery('.cust-tabs #tabAll .bx-pager').css('display','none');
		}
		if(tabOpticount < 2){
			jQuery('.cust-tabs #tabOptimization .bx-pager').css('display','none');
		}
		if(tabAutocount < 2){
			jQuery('.cust-tabs #tabAutomation .bx-pager').css('display','none');
		}
		if(tabQAcount < 2){
			jQuery('.cust-tabs #tabTestQA .bx-pager').css('display','none');
		}
		if(tabStracount < 2){
			jQuery('.cust-tabs #tabStrategy .bx-pager').css('display','none');
		}
		if(tabDevcount < 2){
			jQuery('.cust-tabs #tabDevelopment .bx-pager').css('display','none');
		}
	}
}

jQuery(window).resize(function(){

	var maxHeightevent = -1;
	jQuery('.newsnevent .box .title').each(function() {
	 maxHeightevent = maxHeightevent > jQuery(this).height() ? maxHeightevent : jQuery(this).height();
	});
	jQuery('.newsnevent .box .title').each(function() {
	 jQuery(this).height(maxHeightevent);
	});

	jQuery('.suppaMenu').css('width','auto');
	jQuery('.suppaMenu').css('float','right');

	var sbwidth1 = jQuery('.cust_3 .bx-viewport').width();
	//alert(sbwidth1);
	if(sbwidth1 <=580){
		jQuery('.cust_3 .slide').css('width',sbwidth1);
	}
	else if (sbwidth1>580 && sbwidth1<=900){
		jQuery('.cust_3 .slide').css('width',sbwidth1/2);
	}
	else if (sbwidth1>900 && sbwidth1<=3300){
		jQuery('.cust_3 .slide').css('width',sbwidth1/3);
	}
});
function showHideDiv(id,aid){
	var obj = document.getElementById(id);
	var obj2 = document.getElementById(aid);
	if (obj.style.display=="none"){
		obj.style.display='inline';
		obj2.innerHTML='Read Less';
	} else if(obj.style.display=="inline"){
		obj.style.display='none';
		obj2.innerHTML ='Read More';
	}
}

jQuery.fn.equalize = function(variant) {
	if (variant === undefined) {
          variant = 0;
    }
	var heights = new Array();
	// Loop to get all element heights
	this.each(function() {
		var $this = jQuery(this);
		// Need to let sizes be whatever they want so no overflow on resize
		$this.css('min-height', '0');
		$this.css('max-height', 'none');
		$this.css('height', 'auto');

		// Then add size (no units) to array
 		heights.push($this.height());
	});

	// Find max height of all elements
	var max = Math.max.apply( Math, heights ) + variant;

	// Set all heights to max height
	this.each(function() {
		var $this = jQuery(this);
		$this.css('height', max + 'px');
	});	
}


/* Homepage Features of QMetry to Improve QA Efficiency section */
	jQuery('.tabnav .t').click(function(){
		jQuery('.tabnav .t').removeClass('active');
		jQuery(this).addClass('active');
		
		var dataname = jQuery(this).attr('data-title');
		//alert(dataname);
		jQuery('.tabscontainer .tabscont .tb').removeClass('active');
		
		jQuery('#' +dataname + '.tb').addClass('active');
	});
	jQuery('[data-toggle="popover"]').popover();
	
	jQuery('.tabscont .resp_title a').click(function(){
		jQuery('.tabscont .resp_title a').removeClass('activelink');
		jQuery(this).addClass('activelink');
	
		var dataname = jQuery(this).attr('data-title');
		//alert(dataname);
		jQuery('.tabscontainer .tabscont .tb').removeClass('active');
		jQuery('#' +dataname + '.tb').addClass('active');
	});
	
	jQuery('.tabscont .resp_title').click(function(){
		jQuery('.tabscont .resp_title').removeClass('activelink');
		jQuery(this).addClass('activelink');
	});
	
	jQuery(document).ready(function(){

		jQuery('.suppa_menu_1,.suppa_menu_2,.suppa_menu_3,.suppa_menu_4,.suppa_menu_5,.suppa_menu_6').hover(
			function(event){ //mouse in
				jQuery('#over-lay').removeClass('display-none').addClass('display-one') 
			},
			function(event){ //mouse out
				jQuery('#over-lay').removeClass('display-one').addClass('display-none') 
			}
		)
		
		jQuery('#over-lay').hover(function(){ 
			jQuery('#over-lay').removeClass('display-one'); jQuery('#over-lay').addClass('display-none')  
		})
		
		jQuery('.suppa_menu_7').hover(function(){ 
			jQuery('#over-lay').removeClass('display-one'); jQuery('#over-lay').addClass('display-none')  
		})

		jQuery('.linkedin').click(function(e){
        	e.stopPropagation();
		});
		
	});