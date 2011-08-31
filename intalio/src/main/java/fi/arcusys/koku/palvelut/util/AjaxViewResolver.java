package fi.arcusys.koku.palvelut.util;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * Implement Ajaxview resolver for ajax view
 * @author Jinhua Chen
 *
 */
public class AjaxViewResolver extends AbstractCachingViewResolver  {  
    
    public static final String AJAX_PREFIX = "ajax_";   
    private String ajaxPrefix = AJAX_PREFIX;    

    /* Spring instantiable */
    private View ajaxView;
  
    @Override  
    protected View loadView(String viewName, Locale locale) throws Exception {  
        View view = null;  
        if (viewName.startsWith(AJAX_PREFIX)) {  
            view = ajaxView;  
        }  
        return view;  
    } 
    
    public View getAjaxView() {
        return ajaxView;
    }

    public void setAjaxView(View ajaxView) {
        this.ajaxView = ajaxView;
    }

	public String getAjaxPrefix() {
		return ajaxPrefix;
	}

	public void setAjaxPrefix(String ajaxPrefix) {
		this.ajaxPrefix = ajaxPrefix;
	}
    
}  
