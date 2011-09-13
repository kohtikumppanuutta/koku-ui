package fi.koku.taskmanager.controller;

import java.util.Locale;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * Implements Ajaxview resolver for ajax view
 * @author Jinhua Chen
 * May 11, 2011
 */
public class AjaxViewResolver extends AbstractCachingViewResolver  {  
    
    public static final String AJAX_PREFIX = "ajax_";   
    protected String ajaxPrefix = AJAX_PREFIX;    
    private View ajaxView = new AjaxView ();  
  
    @Override  
    protected View loadView(String viewName, Locale locale) throws Exception {  
        View view = null;  
        if (viewName.startsWith(AJAX_PREFIX)) {  
            view = ajaxView;  
        }  
        return view;  
    }  
   
}  
