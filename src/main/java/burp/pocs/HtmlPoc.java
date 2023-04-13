
package burp.pocs;

import burp.BurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IHttpRequestResponse;
import burp.IParameter;
import burp.IRequestInfo;
import burp.Util;
import java.util.List;

/**
 * HTML CSRF POCs
 * 
 * @author Joaquin R. Martinez <joaquin.ramirez.mtz.lab@gmail.com>
 */
public class HtmlPoc implements IPoc {

	private IExtensionHelpers helpers;
	
	public HtmlPoc(IExtensionHelpers helpers) {
		this.helpers = helpers;
	}
	
    @Override
    public byte[] getPoc(final IHttpRequestResponse request) {
        String lineSep = System.lineSeparator();
        StringBuilder pocString = new StringBuilder();
        IRequestInfo requestInfo = helpers.analyzeRequest(request);
        pocString.append("<!DOCTYPE html>").append(lineSep);
        pocString.append("<html>").append(lineSep)
                .append("  <!-- CSRF PoC - generated by Burp Suite plugin -->").append(lineSep);
        pocString.append("<body>").append(lineSep);
        pocString.append("\t<form method=\"").append(requestInfo.getMethod())
                .append("\" action=\"").append(requestInfo.getUrl().toString()).append("\">").append(lineSep);        
        // params
        List<IParameter> parameters = requestInfo.getParameters();
        parameters.forEach((parameter) -> {
            pocString.append("\t\t<input type=\"text\" name=\"")
                    .append(Util.encodeHTML(helpers.urlDecode(parameter.getName())))
                    .append("\" value=\"").append(Util.encodeHTML(helpers.urlDecode(parameter.getValue())))
                    .append("\">").append(lineSep);
        });
        pocString.append("\t\t<input type=\"submit\" value=\"Send\">").append(lineSep);
        pocString.append("\t</form>").append(lineSep).append("</body>").append(lineSep).append("</html>");
        return pocString.toString().getBytes();
    }
    
}
