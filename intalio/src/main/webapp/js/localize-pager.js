/*

What is this?:
		This code replaces JQuery.Pager-plugins button texts 
		with given parameters. Default parameters 
		for "first,prev,next,last" are "<<,<,>,>>". 


Usage:

		//TODO: describe used params! 

		//Fill params object with your texts
		var params = new Object();
		params.first = 'Ensimmäinen';
		params.prev  = 'Edellinen';
		params.next  = 'Seuraava';
		params.last  = 'Viimeinen';

		...

		//Call method after Pager "PageClick"-method because
		//Pager creates stuff again after every button click (pager-buttons) 
		LocalizeJQueryPager(params);

		That's it!
		
		---Example HTML block---
		<div id="pager">
		<ul class="pages">
			<li class="pgNext pgEmpty">first</li>
			<li class="pgNext pgEmpty">prev</li>
			<li class="page-number pgCurrent">1</li>
			<li class="page-number">2</li>
			<li class="page-number">3</li>
			<li class="pgNext">next</li>
			<li class="pgNext">last</li>
		</ul>
		</div>
		---Example HTML block---
*
*/

LocalizeJQueryPager = function(params) {
            
	  		/*Set default values*/ 
	  		if(!params) params = new Object();
	  		params.xpathselector = !params.xpathselector ? '#pager' : params.xpathselector ;
	  		
	  		/* Basic button texts */
	  		params.first = !params.first ? '<<' : params.first ;
	  		params.prev = !params.prev ? '<' : params.prev ;
	  		params.next = !params.next ? '>' : params.next ;
	  		params.last = !params.last ? '>>' : params.last ;
	  		
	  		/* Button visibility */
	  		params.firstIsVisible = !params.firstIsVisible ? 'true' : params.firstIsVisible ;
	  		params.prevIsVisible  = !params.prevIsVisible ? 'true' : params.prevIsVisible ;
	  		params.nextIsVisible  = !params.nextIsVisible ? 'true' : params.nextIsVisible ;
	  		params.lastIsVisible  = !params.lastIsVisible ? 'true' : params.lastIsVisible ;
	  		params.numbersIsVisible = !params.numbersIsVisible ? 'true' : params.numbersIsVisible ;
	  		
	  		jQuery(params.xpathselector+" li").each(function(i, selected){ 
	  		var text = jQuery(selected).text();
			 	
	  		switch (text) {
	  				case "1":
	  					if( params.pageheaderHTML ){
	  						/* IF defined, Insert HTML before first page button */
	  						jQuery(selected).before(params.pageheaderHTML);
	  					}
	  					if( params.numbersIsVisible=='false') {
		            		jQuery(selected).hide();
		            	}
	  					break;
	  				case "first":
	  					if( params.firstIsVisible=='false') {
	  						jQuery(selected).hide();
	  					}
	  						jQuery(selected).text(params.first);
	  						jQuery(selected).addClass('first');
		            	break;
		            case "prev":
		            	if( params.prevIsVisible=='false') {
	  						jQuery(selected).hide();
	  					}
		            	jQuery(selected).text(params.prev);
		            	jQuery(selected).addClass('prev');
		                break;
		            case "next":
		            	if( params.nextIsVisible=='false') {
	  						jQuery(selected).hide();
	  					}
		            	jQuery(selected).text(params.next);
		            	jQuery(selected).addClass('next');		            	
		                break;
		            case "last":
		            	if( params.lastIsVisible=='false') {
	  						jQuery(selected).hide();
	  					}
		            	jQuery(selected).text(params.last);
		            	jQuery(selected).addClass('last');
		            	break;
		                
		            default:
		            	if( params.numbersIsVisible=='false') {
		            		if ( jQuery(selected).hasClass('page-number') ) {
		            			jQuery(selected).hide();
		            		}
		            	}	            
		            break;
		            	
		        }
	  		});
	  		
  }