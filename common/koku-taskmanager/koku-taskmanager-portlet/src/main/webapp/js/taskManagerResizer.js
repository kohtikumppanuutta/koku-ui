/**
 * 
 * JS Resizer 
 * 
 * @author tturunen
 * 
 */


	var timerOn = 0;
	var iFrameContentPreviousHeight = 0;
	var minHeight = 400;
	var iFrameId = '<portlet:namespace />xforms_iframe';
	
	function startResizer() {
		startResizer(iFrameId);		
	}

	function startResizer(frameId) {
		if (!timerOn) {
			iFrameId = frameId;
			timerOn = 1;
			resizeTimer();
		}
	}
	
	/* Meansure IFrame size */
	function resizeTimer() {
		var iFrameContentHeight = getIFrameBodyHeight(iFrameId);
		if (iFrameContentPreviousHeight != iFrameContentHeight) {
			var newHeight = iFrameContentHeight + 20;
			resizeIFrame(iFrameId, newHeight);
			/* 	We have to ask height again, because Chorme/Safari will change content height on some 
			 *	cases after resizing.
			 */
			iFrameContentPreviousHeight = getIFrameBodyHeight(iFrameId);
		}		
		setTimeout("resizeTimer()", 500 );
	}
			
	function resizeIFrame(id, height) {
		if (height < minHeight) {
			height = minHeight;
		}		
		document.getElementById(id).style.height = height+'px';
	}
	
	function getIFrameBodyHeight(id) {
		var iFrame =  document.getElementById(id);
		if (iFrame == null) {
			return minHeight;
		}
		
		var body;
		 
		if (!isIE()) { 
			 /* Firefox, Safari, Chrome etc. */
			  body = iFrame.contentDocument.getElementsByTagName('body')[0];
		} else { 
			 /*  InternetExplorer */
			 body = iFrame.contentWindow.document.getElementsByTagName('body')[0];
		}
		
		/* IE doesn't seem to be working correctly */
		if (isIE()) {
			if (body == undefined) {
				return iFrameContentPreviousHeight;
			}
			
			/* IE specific fix (for somereason IE can't set CSS styles by itself) */
			var jsx3 = iFrame.contentDocument.getElementById('_jsx_0_3');
			if (jsx3 != undefined || jsx3 != null ) {				
				jsx3.style.display = 'block';
				jsx3.style.height = '100%';
				jsx3.style.overflow = 'visible';
				jsx3.style.position = 'absolute';
				jsx3.style.width = '100%';
			} 
			
			var jsx2 = iFrame.contentDocument.getElementById('_jsx_0_2');
			if (jsx2 != undefined || jsx2 != null ) {				
				jsx2.style.height = '100%';
			}
			
			var jsx1 = iFrame.contentDocument.getElementById('_jsx_0_1');
			if (jsx1 != undefined || jsx1 != null ) {				
				jsx1.style.display = 'block';
				jsx1.style.height = '100%';
				jsx1.style.overflow = 'visible';
				jsx1.style.position = 'absolute';
				jsx1.style.width = '100%';
			}
							
			return body.scrollHeight + (body.offsetHeight - body.clientHeight);
		} else {
			return body.scrollHeight;		
		}
		
	}
	
	function isIE() {
		if (navigator.appName == 'Microsoft Internet Explorer') {
			return true;
		} else {
			return false;
		}
	}