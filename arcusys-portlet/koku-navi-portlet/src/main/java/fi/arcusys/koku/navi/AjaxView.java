package fi.arcusys.koku.navi;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Implements ajax view for ajax response in json format
 * @author Jinhua Chen
 * May 11, 2011
 */
public class AjaxView extends AbstractView {

    private static final String DEFAULT_AJAX_CONTENT_TYPE = "text/plain; charset=UTF-8";

    public AjaxView() {
		super();
		setContentType(DEFAULT_AJAX_CONTENT_TYPE);
	}
    
    @Override
    public String getContentType () {
        String orgiContentType = super.getContentType ();
        if (StringUtils.isEmpty (orgiContentType))
            orgiContentType = DEFAULT_AJAX_CONTENT_TYPE;

        return orgiContentType;
    }

    @Override
    protected void renderMergedOutputModel (Map<String, Object> map, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (map == null || map.isEmpty ()) {
            JSONObject.fromObject ("{}").write (response.getWriter ());
            return;
        }

        JSON json = JSONSerializer.toJSON (map);
        json.write (response.getWriter ());
    }

}
